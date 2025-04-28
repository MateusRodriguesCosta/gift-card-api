package com.costasolutions.giftcards.controllers;

import com.costasolutions.giftcards.dto.balance.BalanceUpdateRequest;
import com.costasolutions.giftcards.dto.card.CreateCardDTO;
import com.costasolutions.giftcards.dto.card.FullCardDTO;
import com.costasolutions.giftcards.dto.expiration.ExpirationUpdateRequest;
import com.costasolutions.giftcards.services.GiftCardManagementService;
import com.costasolutions.giftcards.services.GiftCardTransactionService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/giftcards")
public class GiftCardController {

    private final GiftCardManagementService giftCardManagementService;
    private final GiftCardTransactionService giftCardTransactionService;

    public GiftCardController(GiftCardManagementService giftCardManagementService,
                              GiftCardTransactionService giftCardTransactionService) {
        this.giftCardManagementService = giftCardManagementService;
        this.giftCardTransactionService = giftCardTransactionService;
    }

    @GetMapping
    public ResponseEntity<Page<FullCardDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search
    ) {
        Page<FullCardDTO> giftCards = giftCardManagementService.getAllGiftCards(page, size, search);
        return ResponseEntity.ok(giftCards);
    }

    @GetMapping("/{token}")
    public ResponseEntity<FullCardDTO> getGiftCardByToken(@PathVariable String token) {
        FullCardDTO dto = giftCardManagementService.getGiftCardByToken(token);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/isValid/{token}")
    public ResponseEntity<Boolean> isValid(@PathVariable String token) {
        Boolean isValid = giftCardManagementService.isValidGiftCard(token);
        return ResponseEntity.ok(isValid);
    }

    @PostMapping
    public ResponseEntity<CreateCardDTO> createGiftCard(@Valid @RequestBody CreateCardDTO createCardDTO) {
        CreateCardDTO created = giftCardManagementService.createGiftCard(createCardDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PatchMapping("/cancel/{token}")
    public ResponseEntity<FullCardDTO> cancelGiftCard(@PathVariable String token) {
        FullCardDTO fullCardDTO = giftCardManagementService.cancelGiftCard(token);
        return ResponseEntity.ok(fullCardDTO);
    }

    @PatchMapping("/credit/{token}")
    public ResponseEntity<FullCardDTO> creditGiftCardBalance(@PathVariable String token,
                                                       @RequestBody @Valid BalanceUpdateRequest balanceUpdateRequest) {
        FullCardDTO fullCardDTO = giftCardTransactionService.creditGiftCardBalance(token, balanceUpdateRequest.getAmount(), null);
        return ResponseEntity.ok(fullCardDTO);
    }

    @PatchMapping("/debit/{token}")
    public ResponseEntity<FullCardDTO> debitGiftCardBalance(@PathVariable String token,
                                                       @RequestBody @Valid BalanceUpdateRequest balanceUpdateRequest) {
        FullCardDTO fullCardDTO = giftCardTransactionService.debitGiftCardBalance(token, balanceUpdateRequest.getAmount(), null);
        return ResponseEntity.ok(fullCardDTO);
    }

    @PatchMapping("/exchange/from/{tokenSource}/to/{tokenTarget}")
    public ResponseEntity<List<FullCardDTO>> exchangeGiftCardBalance(@PathVariable String tokenSource,
                                                                     @PathVariable String tokenTarget,
                                                                     @RequestBody @Valid BalanceUpdateRequest balanceUpdateRequest) {
        List<FullCardDTO> fullCardDTOS = giftCardTransactionService.exchangeGiftCardBalance(tokenSource, tokenTarget, balanceUpdateRequest.getAmount());
        return ResponseEntity.ok(fullCardDTOS);
    }

    @PatchMapping("/expiration/{token}")
    public ResponseEntity<FullCardDTO> updateGiftCardExpiration(@PathVariable String token,
                                                                @RequestBody @Valid ExpirationUpdateRequest expirationUpdateRequest) {
        FullCardDTO fullCardDTO = this.giftCardManagementService.updateGiftCardExpiration(token, expirationUpdateRequest);
        return ResponseEntity.ok(fullCardDTO);
    }

}
