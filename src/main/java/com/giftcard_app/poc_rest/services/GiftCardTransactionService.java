package com.giftcard_app.poc_rest.services;

import com.giftcard_app.poc_rest.dto.card.FullCardDTO;
import com.giftcard_app.poc_rest.enums.CardStatus;
import com.giftcard_app.poc_rest.exception.GiftCardNotFoundException;
import com.giftcard_app.poc_rest.exception.InsufficientBalanceException;
import com.giftcard_app.poc_rest.exception.InvalidGiftCardStateException;
import com.giftcard_app.poc_rest.mapper.GiftCardMapper;
import com.giftcard_app.poc_rest.models.GiftCard;
import com.giftcard_app.poc_rest.repositories.GiftCardRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class GiftCardTransactionService {

    private final GiftCardRepository giftCardRepository;
    private final GiftCardMapper giftCardMapper;

    public GiftCardTransactionService(GiftCardRepository giftCardRepository,
                                      GiftCardMapper giftCardMapper) {
        this.giftCardRepository = giftCardRepository;
        this.giftCardMapper = giftCardMapper;
    }

    @Transactional
    public FullCardDTO creditGiftCardBalance(String token, BigDecimal amount) {
        GiftCard giftCard = getActiveGiftCardByToken(token);
        validateAmount(amount);

        giftCard.setBalance(giftCard.getBalance().add(amount));
        GiftCard savedCard = giftCardRepository.save(giftCard);
        return giftCardMapper.toFullDTO(savedCard);
    }

    @Transactional
    public FullCardDTO debitGiftCardBalance(String token, BigDecimal amount) {
        GiftCard giftCard = getActiveGiftCardByToken(token);
        validateAmount(amount);

        if (giftCard.getBalance().compareTo(amount) < 0) {
            throw new InsufficientBalanceException("Insufficient balance on the gift card");
        }

        giftCard.setBalance(giftCard.getBalance().subtract(amount));
        GiftCard savedCard = giftCardRepository.save(giftCard);
        return giftCardMapper.toFullDTO(savedCard);
    }

    @Transactional
    public List<FullCardDTO> exchangeGiftCardBalance(String sourceToken, String targetToken, BigDecimal amount) {
        FullCardDTO updatedSource = debitGiftCardBalance(sourceToken, amount);
        FullCardDTO updatedTarget = creditGiftCardBalance(targetToken, amount);
        return List.of(updatedSource, updatedTarget);
    }

    // Helper Methods

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
}
