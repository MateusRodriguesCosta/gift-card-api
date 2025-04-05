package com.giftcard_app.poc_rest.mapper;

import com.giftcard_app.poc_rest.dto.card.CreateCardDTO;
import com.giftcard_app.poc_rest.dto.card.FullCardDTO;
import com.giftcard_app.poc_rest.models.GiftCard;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GiftCardMapper {
    CreateCardDTO toCreateDTO(GiftCard giftCard);
    FullCardDTO toFullDTO(GiftCard giftCard);
    GiftCard toEntity(CreateCardDTO createCardDTO);
    GiftCard toEntity(FullCardDTO fullCardDTO);
}
