package com.giftcard_app.poc_rest.controllers;

import com.giftcard_app.poc_rest.dto.card.CreateCardDTO;
import com.giftcard_app.poc_rest.dto.card.FullCardDTO;
import com.giftcard_app.poc_rest.services.GiftCardService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/giftcard")
public class GiftCardController {

    private final GiftCardService giftCardService;

    public GiftCardController(GiftCardService giftCardService) {
        this.giftCardService = giftCardService;
    }

    @GetMapping
    public ResponseEntity<List<FullCardDTO>> getAll() {
        List<FullCardDTO> giftCards = giftCardService.getAllGiftCards();
        return ResponseEntity.ok(giftCards);
    }

    @GetMapping("/{cardNumber}")
    public ResponseEntity<FullCardDTO> getGiftCardByCardNumber(@PathVariable String cardNumber) {
        FullCardDTO dto = giftCardService.getGiftCardByCardNumber(cardNumber);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/isValid/{cardNumber}")
    public ResponseEntity<Boolean> isValid(@PathVariable String cardNumber) {
        Boolean isValid = this.giftCardService.isValidGiftCardNumber(cardNumber);
        return ResponseEntity.ok(isValid);
    }

    @PostMapping
    public ResponseEntity<CreateCardDTO> createGiftCard(@Valid @RequestBody CreateCardDTO createCardDTO) {
        CreateCardDTO created = giftCardService.createGiftCard(createCardDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

}
