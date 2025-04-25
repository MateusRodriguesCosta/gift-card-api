package com.costasolutions.giftcards.mapper;

import com.costasolutions.giftcards.dto.card.CreateCardDTO;
import com.costasolutions.giftcards.dto.card.FullCardDTO;
import com.costasolutions.giftcards.models.GiftCard;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface GiftCardMapper {
    CreateCardDTO toCreateDTO(GiftCard giftCard);
    @Mapping(source = "cardNumber", target = "maskedCardNumber")
    FullCardDTO toFullDTO(GiftCard giftCard);
    GiftCard toEntity(CreateCardDTO createCardDTO);
    GiftCard toEntity(FullCardDTO fullCardDTO);
}
