package com.costasolutions.giftcards.repositories;

import com.costasolutions.giftcards.models.GiftCard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface GiftCardRepository extends JpaRepository<GiftCard, Long>, JpaSpecificationExecutor<GiftCard> {

    Optional<GiftCard> findByToken(String token);
}
