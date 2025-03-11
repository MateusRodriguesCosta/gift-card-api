package com.incomm_app.incomm_poc_rest.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.incomm_app.incomm_poc_rest.models.GiftCard;
import com.incomm_app.incomm_poc_rest.repositories.GiftCardRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/giftcards")
public class GiftCardController {
    
    @Autowired
    private GiftCardRepository giftCardRepository;

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
