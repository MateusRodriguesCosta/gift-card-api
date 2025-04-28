package com.costasolutions.giftcards.repositories;

import com.costasolutions.giftcards.models.GiftCard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface GiftCardRepository extends JpaRepository<GiftCard, Long> {

    Optional<GiftCard> findByToken(String token);

    @Query("select g from GiftCard g " +
            "where (:search is null or " +
            "lower(g.token) like lower(concat('%', :search, '%')) " +
            "or lower(g.region) like lower(concat('%', :search, '%')))")
    Page<GiftCard> search(@Param("search") String search, Pageable pageable);


}
