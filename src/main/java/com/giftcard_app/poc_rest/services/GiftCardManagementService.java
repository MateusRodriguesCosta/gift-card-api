package com.giftcard_app.poc_rest.services;

import com.giftcard_app.poc_rest.components.CardNumberGenerator;
import com.giftcard_app.poc_rest.components.CardTokenGenerator;
import com.giftcard_app.poc_rest.dto.card.CreateCardDTO;
import com.giftcard_app.poc_rest.dto.card.FullCardDTO;
import com.giftcard_app.poc_rest.enums.CardStatus;
import com.giftcard_app.poc_rest.exception.GiftCardNotFoundException;
import com.giftcard_app.poc_rest.exception.InvalidGiftCardStateException;
import com.giftcard_app.poc_rest.mapper.GiftCardMapper;
import com.giftcard_app.poc_rest.models.GiftCard;
import com.giftcard_app.poc_rest.repositories.GiftCardRepository;
import jakarta.transaction.Transactional;
import org.apache.commons.validator.routines.checkdigit.LuhnCheckDigit;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GiftCardManagementService {

    private final GiftCardRepository giftCardRepository;
    private final GiftCardMapper giftCardMapper;
    private final CardTokenGenerator cardTokenGenerator;
    private final CardNumberGenerator cardNumberGenerator;

    private final LuhnCheckDigit luhn = new LuhnCheckDigit();

    public GiftCardManagementService(GiftCardRepository giftCardRepository,
                                     GiftCardMapper giftCardMapper,
                                     CardTokenGenerator cardTokenGenerator,
                                     CardNumberGenerator cardNumberGenerator) {
        this.giftCardRepository = giftCardRepository;
        this.giftCardMapper = giftCardMapper;
        this.cardTokenGenerator = cardTokenGenerator;
        this.cardNumberGenerator = cardNumberGenerator;
    }

    /**
     * Creates a new gift card.
     */
    public CreateCardDTO createGiftCard(CreateCardDTO createCardDTO) {
        GiftCard giftCard = giftCardMapper.toEntity(createCardDTO);
        giftCard.setToken(cardTokenGenerator.generateToken());
        giftCard.setCardNumber(cardNumberGenerator.generateCardNumber());
        giftCard.setIssueDate(LocalDateTime.now());
        giftCard.setStatus(CardStatus.ACTIVE);

        GiftCard savedGiftCard = giftCardRepository.save(giftCard);
        return giftCardMapper.toCreateDTO(savedGiftCard);
    }

    /**
     * Cancels a gift card if it is in a state that allows cancellation.
     */
    @Transactional
    public FullCardDTO cancelGiftCard(String token) {
        GiftCard giftCard = giftCardRepository.findByToken(token)
                .orElseThrow(() -> new GiftCardNotFoundException("Gift card not found"));

        if (giftCard.getStatus() == CardStatus.ACTIVE ||
                giftCard.getStatus() == CardStatus.PENDING ||
                giftCard.getStatus() == CardStatus.EXPIRED) {
            giftCard.setStatus(CardStatus.CANCELLED);
            GiftCard savedCard = giftCardRepository.save(giftCard);
            return giftCardMapper.toFullDTO(savedCard);
        } else {
            throw new InvalidGiftCardStateException("Gift card is not in a cancellable state");
        }
    }

    /**
     * Retrieves a gift card's details by token.
     */
    public FullCardDTO getGiftCardByToken(String token) {
        GiftCard giftCard = giftCardRepository.findByToken(token)
                .orElseThrow(() -> new GiftCardNotFoundException("Gift card not found"));
        return giftCardMapper.toFullDTO(giftCard);
    }

    /**
     * Retrieves all gift cards.
     */
    public List<FullCardDTO> getAllGiftCards() {
        return giftCardRepository.findAll()
                .stream()
                .map(giftCardMapper::toFullDTO)
                .collect(Collectors.toList());
    }

    /**
     * Validates the gift card number
     */
    public boolean isValidGiftCard(String token) {
        return giftCardRepository.findByToken(token)
                .map(giftCard -> luhn.isValid(giftCard.getCardNumber()))
                .orElse(false);
    }
}
