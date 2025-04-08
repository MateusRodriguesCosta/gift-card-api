package com.giftcard_app.poc_rest.controllers;

import com.giftcard_app.poc_rest.dto.balance.BalanceUpdateRequest;
import com.giftcard_app.poc_rest.dto.card.CreateCardDTO;
import com.giftcard_app.poc_rest.dto.card.FullCardDTO;
import com.giftcard_app.poc_rest.services.GiftCardService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/giftcards")
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

    @GetMapping("/{token}")
    public ResponseEntity<FullCardDTO> getGiftCardByToken(@PathVariable String token) {
        FullCardDTO dto = giftCardService.getGiftCardByToken(token);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/isValid/{token}")
    public ResponseEntity<Boolean> isValid(@PathVariable String token) {
        Boolean isValid = this.giftCardService.isValidGiftCard(token);
        return ResponseEntity.ok(isValid);
    }

    @PostMapping
    public ResponseEntity<CreateCardDTO> createGiftCard(@Valid @RequestBody CreateCardDTO createCardDTO) {
        CreateCardDTO created = giftCardService.createGiftCard(createCardDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PatchMapping("/cancel/{token}")
    public ResponseEntity<String> cancelGiftCard(@PathVariable String token) {
        giftCardService.cancelGiftCard(token);
        return ResponseEntity.ok("Card cancelled successfully");
    }

    @PatchMapping("/credit/{token}")
    public ResponseEntity<FullCardDTO> creditGiftCardBalance(@PathVariable String token,
                                                       @RequestBody @Valid BalanceUpdateRequest balanceUpdateRequest) {
        FullCardDTO fullCardDTO = giftCardService.creditGiftCardBalance(token, balanceUpdateRequest.getAmount());
        return ResponseEntity.ok(fullCardDTO);
    }

    @PatchMapping("/debit/{token}")
    public ResponseEntity<FullCardDTO> debitGiftCardBalance(@PathVariable String token,
                                                       @RequestBody @Valid BalanceUpdateRequest balanceUpdateRequest) {
        FullCardDTO fullCardDTO = giftCardService.debitGiftCardBalance(token, balanceUpdateRequest.getAmount());
        return ResponseEntity.ok(fullCardDTO);
    }

    @PatchMapping("/exchange/from/{tokenSource}/to/{tokenTarget}")
    public ResponseEntity<List<FullCardDTO>> exchangeGiftCardBalance(@PathVariable String tokenSource,
                                                                     @PathVariable String tokenTarget,
                                                                     @RequestBody @Valid BalanceUpdateRequest balanceUpdateRequest) {
        List<FullCardDTO> fullCardDTOS = giftCardService.exchangeGiftCardBalance(tokenSource, tokenTarget, balanceUpdateRequest.getAmount());
        return ResponseEntity.ok(fullCardDTOS);
    }

}
