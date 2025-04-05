package com.giftcard_app.poc_rest.controllers;

import com.giftcard_app.poc_rest.dto.GiftCardDTO;
import com.giftcard_app.poc_rest.services.GiftCardService;
import io.swagger.v3.oas.annotations.Operation;
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
    public ResponseEntity<List<GiftCardDTO>> getAll() {
        List<GiftCardDTO> giftCards = giftCardService.getAllGiftCards();
        return ResponseEntity.ok(giftCards);
    }

    @GetMapping("/{cardNumber}")
    public ResponseEntity<GiftCardDTO> getGiftCardByCardNumber(@PathVariable String cardNumber) {
        GiftCardDTO dto = giftCardService.getGiftCardByCardNumber(cardNumber);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/isValid/{cardNumber}")
    public ResponseEntity<Boolean> isValid(@RequestParam String cardNumber) {
        Boolean isValid = this.giftCardService.isValidGiftCardNumber(cardNumber);
        return ResponseEntity.ok(isValid);
    }

    @PostMapping
    public ResponseEntity<GiftCardDTO> createGiftCard(@Valid @RequestBody GiftCardDTO giftCardDTO) {
        GiftCardDTO created = giftCardService.createGiftCard(giftCardDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }


}
