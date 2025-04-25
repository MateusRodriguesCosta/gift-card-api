package com.costasolutions.giftcards.services;

import com.costasolutions.giftcards.dto.card.FullCardDTO;
import com.costasolutions.giftcards.enums.CardStatus;
import com.costasolutions.giftcards.enums.TransactionType;
import com.costasolutions.giftcards.exception.GiftCardNotFoundException;
import com.costasolutions.giftcards.exception.InsufficientBalanceException;
import com.costasolutions.giftcards.exception.InvalidGiftCardStateException;
import com.costasolutions.giftcards.mapper.GiftCardMapper;
import com.costasolutions.giftcards.models.GiftCard;
import com.costasolutions.giftcards.models.Transaction;
import com.costasolutions.giftcards.repositories.GiftCardRepository;
import com.costasolutions.giftcards.repositories.TransactionRepository;
import io.micrometer.common.lang.Nullable;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final Logger logger = LoggerFactory.getLogger(GiftCardTransactionService.class);

    public GiftCardTransactionService(GiftCardRepository giftCardRepository,
                                      TransactionRepository transactionRepository,
                                      GiftCardMapper giftCardMapper) {
        this.giftCardRepository = giftCardRepository;
        this.transactionRepository = transactionRepository;
        this.giftCardMapper = giftCardMapper;
    }

    @Transactional
    public FullCardDTO creditGiftCardBalance(String token, BigDecimal amount, @Nullable UUID exchangeId) {
        GiftCard giftCard = getActiveGiftCardByToken(token);
        validateAmount(amount);

        giftCard.setBalance(giftCard.getBalance().add(amount));
        GiftCard savedCard = giftCardRepository.save(giftCard);
        recordTransaction(giftCard, TransactionType.CREDIT, amount, exchangeId);
        logger.info("Credit gift card balance: {}", giftCard);

        return giftCardMapper.toFullDTO(savedCard);
    }

    @Transactional
    public FullCardDTO debitGiftCardBalance(String token, BigDecimal amount, @Nullable UUID exchangeId) {
        GiftCard giftCard = getActiveGiftCardByToken(token);
        validateAmount(amount);

        if (giftCard.getBalance().compareTo(amount) < 0) {
            logger.error("Insufficient balance on the gift card to debit operation");
            throw new InsufficientBalanceException("Insufficient balance on the gift card");
        }

        giftCard.setBalance(giftCard.getBalance().subtract(amount));
        GiftCard savedCard = giftCardRepository.save(giftCard);
        recordTransaction(giftCard, TransactionType.DEBIT, amount, exchangeId);
        logger.info("Debit gift card balance: {}", giftCard);

        return giftCardMapper.toFullDTO(savedCard);
    }

    @Transactional
    public List<FullCardDTO> exchangeGiftCardBalance(String sourceToken, String targetToken, BigDecimal amount) {
        UUID exchangeId = UUID.randomUUID();
        logger.debug("Starting to exchange gift card balance from {} to {}", sourceToken, targetToken);
        FullCardDTO updatedSource = debitGiftCardBalance(sourceToken, amount, exchangeId);
        FullCardDTO updatedTarget = creditGiftCardBalance(targetToken, amount, exchangeId);
        logger.info("Exchanged gift cards balance: from {} to {}", updatedSource, updatedTarget);
        return List.of(updatedSource, updatedTarget);
    }

    private GiftCard getActiveGiftCardByToken(String token) {
        GiftCard giftCard = giftCardRepository.findByToken(token)
                .orElseThrow(() -> new GiftCardNotFoundException("Gift card not found"));
        if (giftCard.getStatus() != CardStatus.ACTIVE) {
            logger.error("Gift card is not in active state");
            throw new InvalidGiftCardStateException("Gift card is not active");
        }
        logger.info("Get active gift card: {}", giftCard);
        return giftCard;
    }

    private void validateAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            logger.error("Invalid amount for gift card");
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
        transactionRepository.save(transaction);
    }

}
