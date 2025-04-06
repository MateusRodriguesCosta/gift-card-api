package com.giftcard_app.poc_rest.repositories;

import com.giftcard_app.poc_rest.models.GiftCard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GiftCardRepository extends JpaRepository<GiftCard, Long> {
    Optional<GiftCard> findByToken(String token);


}
