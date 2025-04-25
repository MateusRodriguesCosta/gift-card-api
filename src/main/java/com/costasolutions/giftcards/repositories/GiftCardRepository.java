package com.costasolutions.giftcards.repositories;

import com.costasolutions.giftcards.models.GiftCard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GiftCardRepository extends JpaRepository<GiftCard, Long> {
    Optional<GiftCard> findByToken(String token);


}
