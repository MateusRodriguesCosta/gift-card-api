package com.giftcard_app.poc_rest.services;

import com.giftcard_app.poc_rest.components.CardTokenGenerator;
import com.giftcard_app.poc_rest.dto.card.CreateCardDTO;
import com.giftcard_app.poc_rest.dto.card.FullCardDTO;
import com.giftcard_app.poc_rest.enums.CardStatus;
import com.giftcard_app.poc_rest.exception.GiftCardNotFoundException;
import com.giftcard_app.poc_rest.exception.InsufficientBalanceException;
import com.giftcard_app.poc_rest.exception.InvalidGiftCardStateException;
import com.giftcard_app.poc_rest.mapper.GiftCardMapper;
import com.giftcard_app.poc_rest.models.GiftCard;
import com.giftcard_app.poc_rest.repositories.GiftCardRepository;
import jakarta.transaction.Transactional;
import org.apache.commons.validator.routines.checkdigit.CheckDigitException;
import org.apache.commons.validator.routines.checkdigit.LuhnCheckDigit;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GiftCardService {

    private static final String DIGITS = "0123456789";
    private static final int BASE_NUMBER_LENGTH = 15;
    private static final SecureRandom RANDOM = new SecureRandom();
    private final LuhnCheckDigit luhn = new LuhnCheckDigit();

    private final GiftCardRepository giftCardRepository;
    private final GiftCardMapper giftCardMapper;
    private final CardTokenGenerator cardTokenGenerator;

    public GiftCardService(GiftCardRepository giftCardRepository,
                           GiftCardMapper giftCardMapper,
                           CardTokenGenerator cardTokenGenerator) {
        this.giftCardRepository = giftCardRepository;
        this.giftCardMapper = giftCardMapper;
        this.cardTokenGenerator = cardTokenGenerator;
    }

    public List<FullCardDTO> getAllGiftCards() {
        return giftCardRepository.findAll()
                .stream()
                .map(giftCardMapper::toFullDTO)
                .collect(Collectors.toList());
    }

    public FullCardDTO getGiftCardByToken(String token) {
        GiftCard giftCard = giftCardRepository.findByToken(token)
                .orElseThrow(() -> new GiftCardNotFoundException("Gift card not found"));
        return giftCardMapper.toFullDTO(giftCard);
    }

    public CreateCardDTO createGiftCard(CreateCardDTO createCardDTO) {
        GiftCard giftCard = giftCardMapper.toEntity(createCardDTO);
        giftCard.setToken(cardTokenGenerator.generateToken());
        giftCard.setCardNumber(this.generateGiftCardNumber());
        giftCard.setIssueDate(LocalDateTime.now());
        giftCard.setStatus(CardStatus.ACTIVE);
        GiftCard savedGiftCard = giftCardRepository.save(giftCard);

        return giftCardMapper.toCreateDTO(savedGiftCard);
    }

    @Transactional
    public void cancelGiftCard(String token) {
        GiftCard giftCard = giftCardRepository.findByToken(token)
                .orElseThrow(() -> new GiftCardNotFoundException("Gift card not found"));

        if (giftCard.getStatus() == CardStatus.ACTIVE || giftCard.getStatus() == CardStatus.PENDING || giftCard.getStatus() == CardStatus.EXPIRED) {
           giftCard.setStatus(CardStatus.CANCELLED);
           giftCardRepository.save(giftCard);
        } else {
            throw new InvalidGiftCardStateException("Gift card is not active");
        }
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

    /**
     * Generate gift card number from a random base and the check digit
     * @return Full gift card number
     */
    private String generateGiftCardNumber() {
        StringBuilder baseNumber = new StringBuilder();

        for (int i = 0; i < BASE_NUMBER_LENGTH; i++) {
            int index = RANDOM.nextInt(DIGITS.length());
            baseNumber.append(DIGITS.charAt(index));
        }

        return this.appendCheckDigit(baseNumber.toString());
    }

    public boolean isValidGiftCard(String token) {
        GiftCard giftCard = giftCardRepository.findByToken((token))
                .orElseThrow(() -> new RuntimeException("Gift card not found"));
        return luhn.isValid(giftCard.getCardNumber());
    }

    private String appendCheckDigit(String baseNumber) {
        try {
            return baseNumber + luhn.calculate(baseNumber);
        } catch (CheckDigitException e) {
            throw new RuntimeException(e);
        }
    }

}
