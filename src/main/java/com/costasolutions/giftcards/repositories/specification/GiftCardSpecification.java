package com.costasolutions.giftcards.repositories.specification;

import com.costasolutions.giftcards.models.GiftCard;
import org.springframework.data.jpa.domain.Specification;

public class GiftCardSpecification {

    public static Specification<GiftCard> filterByTokenCardNumberAndStatus(String search) {
        String term = (search == null || search.isBlank())
                ? null
                : search.toLowerCase();

        Specification<GiftCard> spec = Specification.where(null);

        if (term != null) {
            spec = spec
                    .or((root, query, cb) ->
                            cb.like(cb.lower(root.get("token")), "%" + term + "%"))
                    .or((root, query, cb) ->
                            cb.like(cb.lower(root.get("cardNumber")), "%" + term + "%"))
                    .or((root, query, cb) ->
                            cb.like(cb.lower(root.get("status")), "%" + term + "%"));
        }

        return spec;
    }
}
