package com.incomm_app.incomm_poc_rest.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.incomm_app.incomm_poc_rest.models.GiftCard;

public interface GiftCardRepository extends JpaRepository<GiftCard, Long> {
    Optional<GiftCard> findByCode(String code);
    
}
