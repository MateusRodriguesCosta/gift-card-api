package com.giftcard_app.poc_rest.services;

import com.giftcard_app.poc_rest.dto.card.FullCardDTO;
import com.giftcard_app.poc_rest.elastic.TransactionDocument;
import com.giftcard_app.poc_rest.enums.CardStatus;
import com.giftcard_app.poc_rest.enums.TransactionType;
import com.giftcard_app.poc_rest.exception.GiftCardNotFoundException;
import com.giftcard_app.poc_rest.exception.InsufficientBalanceException;
import com.giftcard_app.poc_rest.exception.InvalidGiftCardStateException;
import com.giftcard_app.poc_rest.mapper.GiftCardMapper;
import com.giftcard_app.poc_rest.models.GiftCard;
import com.giftcard_app.poc_rest.models.Transaction;
import com.giftcard_app.poc_rest.repositories.GiftCardRepository;
import com.giftcard_app.poc_rest.repositories.TransactionRepository;
import com.giftcard_app.poc_rest.repositories.TransactionSearchRepository;
import io.micrometer.common.lang.Nullable;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class GiftCardTransactionService {

    private final TransactionRepository transactionRepository;
    private final GiftCardRepository giftCardRepository;
    private final GiftCardMapper giftCardMapper;
    private final TransactionSearchRepository transactionSearchRepository;

    public GiftCardTransactionService(GiftCardRepository giftCardRepository,
                                      TransactionRepository transactionRepository,
                                      GiftCardMapper giftCardMapper, TransactionSearchRepository transactionSearchRepository) {
        this.giftCardRepository = giftCardRepository;
        this.transactionRepository = transactionRepository;
        this.giftCardMapper = giftCardMapper;
        this.transactionSearchRepository = transactionSearchRepository;
    }

    @Transactional
    public FullCardDTO creditGiftCardBalance(String token, BigDecimal amount, @Nullable UUID exchangeId) {
        GiftCard giftCard = getActiveGiftCardByToken(token);
        validateAmount(amount);

        giftCard.setBalance(giftCard.getBalance().add(amount));
        GiftCard savedCard = giftCardRepository.save(giftCard);
        recordTransaction(giftCard, TransactionType.CREDIT, amount, exchangeId);

        return giftCardMapper.toFullDTO(savedCard);
    }

    @Transactional
    public FullCardDTO debitGiftCardBalance(String token, BigDecimal amount, @Nullable UUID exchangeId) {
        GiftCard giftCard = getActiveGiftCardByToken(token);
        validateAmount(amount);

        if (giftCard.getBalance().compareTo(amount) < 0) {
            throw new InsufficientBalanceException("Insufficient balance on the gift card");
        }

        giftCard.setBalance(giftCard.getBalance().subtract(amount));
        GiftCard savedCard = giftCardRepository.save(giftCard);
        recordTransaction(giftCard, TransactionType.DEBIT, amount, exchangeId);

        return giftCardMapper.toFullDTO(savedCard);
    }

    @Transactional
    public List<FullCardDTO> exchangeGiftCardBalance(String sourceToken, String targetToken, BigDecimal amount) {
        UUID exchangeId = UUID.randomUUID();
        FullCardDTO updatedSource = debitGiftCardBalance(sourceToken, amount, exchangeId);
        FullCardDTO updatedTarget = creditGiftCardBalance(targetToken, amount, exchangeId);
        return List.of(updatedSource, updatedTarget);
    }

    private GiftCard getActiveGiftCardByToken(String token) {
        GiftCard giftCard = giftCardRepository.findByToken(token)
                .orElseThrow(() -> new GiftCardNotFoundException("Gift card not found"));
        if (giftCard.getStatus() != CardStatus.ACTIVE) {
            throw new InvalidGiftCardStateException("Gift card is not active");
        }
        return giftCard;
    }

    private void validateAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
    }

    private void recordTransaction(GiftCard giftCard, TransactionType type, BigDecimal amount, UUID exchangeId) {
        Transaction transaction = Transaction.builder()
                .transactionType(type)
                .transactionAmount(amount)
                .transactionDateTime(LocalDateTime.now())
                .exchangeId(exchangeId)
                .giftCard(giftCard)
                .build();

        Transaction savedTransaction = transactionRepository.save(transaction);
        TransactionDocument doc = mapToDocument(savedTransaction);
        transactionSearchRepository.save(doc);
    }

    private TransactionDocument mapToDocument(Transaction transaction) {
        return TransactionDocument.builder()
                .id(transaction.getId())
                .transactionType(transaction.getTransactionType())
                .transactionAmount(transaction.getTransactionAmount())
                .transactionDateTime(transaction.getTransactionDateTime())
                .exchangeId(transaction.getExchangeId())
                .giftCardToken(transaction.getGiftCard().getToken())
                .build();
    }
}
