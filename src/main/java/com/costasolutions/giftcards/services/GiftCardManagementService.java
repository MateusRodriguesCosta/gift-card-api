package com.costasolutions.giftcards.services;

import com.costasolutions.giftcards.components.CardNumberGenerator;
import com.costasolutions.giftcards.components.CardTokenGenerator;
import com.costasolutions.giftcards.dto.card.CreateCardDTO;
import com.costasolutions.giftcards.dto.card.FullCardDTO;
import com.costasolutions.giftcards.dto.expiration.ExpirationUpdateRequest;
import com.costasolutions.giftcards.enums.CardStatus;
import com.costasolutions.giftcards.exception.GiftCardNotFoundException;
import com.costasolutions.giftcards.exception.InvalidGiftCardStateException;
import com.costasolutions.giftcards.mapper.GiftCardMapper;
import com.costasolutions.giftcards.models.GiftCard;
import com.costasolutions.giftcards.repositories.GiftCardRepository;
import jakarta.transaction.Transactional;
import org.apache.commons.validator.routines.checkdigit.LuhnCheckDigit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class GiftCardManagementService {

    private final GiftCardRepository giftCardRepository;
    private final GiftCardMapper giftCardMapper;
    private final CardTokenGenerator cardTokenGenerator;
    private final CardNumberGenerator cardNumberGenerator;
    private final Logger logger = LoggerFactory.getLogger(GiftCardManagementService.class);

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
        logger.info("Created gift card: {}", savedGiftCard);
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
            GiftCard savedGiftCard = giftCardRepository.save(giftCard);
            logger.info("Cancelled gift card: {}", savedGiftCard);
            return giftCardMapper.toFullDTO(savedGiftCard);
        } else {
            logger.error("Invalid gift card status for cancel operation");
            throw new InvalidGiftCardStateException("Gift card is not in a cancellable state");
        }
    }

    /**
     * Retrieves a gift card's details by token.
     */
    public FullCardDTO getGiftCardByToken(String token) {
        GiftCard giftCard = giftCardRepository.findByToken(token)
                .orElseThrow(() -> new GiftCardNotFoundException("Gift card not found"));
        logger.info("Retrieved gift card: {}", giftCard);
        return giftCardMapper.toFullDTO(giftCard);
    }

    /**
     * Retrieves all gift cards.
     */
    public Page<FullCardDTO> getAllGiftCards(int page, int size, String search) {
        Pageable pageable = PageRequest.of(page, size);
        Page<GiftCard> entityPage = giftCardRepository.search(search, pageable);
        return entityPage.map(giftCardMapper::toFullDTO);
    }

    /**
     * Validates the gift card number
     */
    public boolean isValidGiftCard(String token) {
        return giftCardRepository.findByToken(token)
                .map(giftCard -> luhn.isValid(giftCard.getCardNumber()))
                .orElse(false);
    }

    /**
     * Updates an active gift card's expiration date
     */
    public FullCardDTO updateGiftCardExpiration(String token, ExpirationUpdateRequest expirationUpdateRequest) {
        GiftCard giftCard = giftCardRepository.findByToken(token)
                .orElseThrow(() -> new GiftCardNotFoundException("Gift card not found"));

        if (giftCard.getStatus() == CardStatus.ACTIVE) {
            giftCard.setExpiryDate(expirationUpdateRequest.getExpirationDate());
            GiftCard savedGiftCard = giftCardRepository.save(giftCard);
            logger.info("Updated gift card expiration date: {}", savedGiftCard);
            return giftCardMapper.toFullDTO(savedGiftCard);
        } else {
            logger.error("Invalid gift card status for expiration update operation");
            throw new InvalidGiftCardStateException("Gift card is not in an active state");
        }
    }
}
