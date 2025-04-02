package com.giftcard_app.poc_rest.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.giftcard_app.poc_rest.models.GiftCard;

public interface GiftCardRepository extends JpaRepository<GiftCard, Long> {
    Optional<GiftCard> findByCardNumber(String code);
    
}
