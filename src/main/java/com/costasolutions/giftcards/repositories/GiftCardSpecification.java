package com.costasolutions.giftcards.repositories;

import com.costasolutions.giftcards.enums.CardStatus;
import com.costasolutions.giftcards.models.GiftCard;
import org.springframework.data.jpa.domain.Specification;

public class GiftCardSpecification {
    public static Specification<GiftCard> hasToken(String token) {
        return (root, query, cb) ->
                token == null ? null
                        : cb.like(cb.lower(root.get("token")), "%" + token + "%");
    }

    public static Specification<GiftCard> hasRegion(String region) {
        return (root, query, cb) ->
                region == null ? null
                        : cb.equal(cb.lower(root.get("region")), region);
    }

    public static Specification<GiftCard> hasStatus(CardStatus status) {
        return (root, query, cb) ->
                status == null ? null
                        : cb.equal(root.get("status"), status);
    }

    public static Specification<GiftCard> filterBy(String search) {
        return Specification
                .where(hasToken(search))
                .or(hasRegion(search))
                .or((root, query, cb) ->
                        cb.like(cb.lower(root.get("status")), "%" + search + "%")
                );
    }
}
