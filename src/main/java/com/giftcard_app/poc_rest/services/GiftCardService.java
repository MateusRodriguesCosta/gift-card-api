package com.giftcard_app.poc_rest.services;

import com.giftcard_app.poc_rest.dto.GiftCardDTO;
import com.giftcard_app.poc_rest.mapper.GiftCardMapper;
import com.giftcard_app.poc_rest.models.GiftCard;
import com.giftcard_app.poc_rest.repositories.GiftCardRepository;
import org.apache.commons.validator.routines.checkdigit.CheckDigitException;
import org.apache.commons.validator.routines.checkdigit.LuhnCheckDigit;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
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

    public GiftCardService(GiftCardRepository giftCardRepository, GiftCardMapper giftCardMapper) {
        this.giftCardRepository = giftCardRepository;
        this.giftCardMapper = giftCardMapper;
    }

    public List<GiftCardDTO> getAllGiftCards() {
        return giftCardRepository.findAll()
                .stream()
                .map(giftCardMapper::toDTO)
                .collect(Collectors.toList());
    }

    public GiftCardDTO getGiftCardByCardNumber(String cardNumber) {
        GiftCard giftCard = giftCardRepository.findByCardNumber(cardNumber)
                .orElseThrow(() -> new RuntimeException("GiftCard not found"));
        return giftCardMapper.toDTO(giftCard);
    }

    public GiftCardDTO createGiftCard(GiftCardDTO giftCardDTO) {
        GiftCard giftCard = giftCardMapper.toEntity(giftCardDTO);
        giftCard.cardNumber = this.generateGiftCardNumber();
        // TODO set issue date and status
        GiftCard savedGiftCard = giftCardRepository.save(giftCard);

        return giftCardMapper.toDTO(savedGiftCard);
    }

    /**
     * Generate gift card number from a random base and the check digit
     * @return Full gift card number
     */
    public String generateGiftCardNumber() {
        StringBuilder baseNumber = new StringBuilder();

        for (int i = 0; i < BASE_NUMBER_LENGTH; i++) {
            int index = RANDOM.nextInt(DIGITS.length());
            baseNumber.append(DIGITS.charAt(index));
        }

        return this.appendCheckDigit(baseNumber.toString());
    }

    public boolean isValidGiftCardNumber(String giftCardNumber) {
        return luhn.isValid(giftCardNumber);
    }

    public String appendCheckDigit(String baseNumber) {
        try {
            return baseNumber + luhn.calculate(baseNumber);
        } catch (CheckDigitException e) {
            throw new RuntimeException(e);
        }
    }

}
