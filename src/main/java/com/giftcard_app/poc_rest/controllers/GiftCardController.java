package com.giftcard_app.poc_rest.controllers;

import com.giftcard_app.poc_rest.models.GiftCard;
import com.giftcard_app.poc_rest.repositories.GiftCardRepository;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/giftcards")
public class GiftCardController {
    
    @Autowired
    private GiftCardRepository giftCardRepository;


    @Operation(summary = "Get all registered gift cards")
    @GetMapping
    public List<GiftCard> getAll() {
        return giftCardRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<GiftCard> createGiftCard(@Valid @RequestBody GiftCard giftCard) {
        GiftCard saved = giftCardRepository.save(giftCard);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GiftCard> getGiftCardById(@PathVariable Long id) {
        return giftCardRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
