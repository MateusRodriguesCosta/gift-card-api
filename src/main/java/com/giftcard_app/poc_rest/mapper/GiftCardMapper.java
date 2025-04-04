package com.giftcard_app.poc_rest.mapper;

import com.giftcard_app.poc_rest.dto.GiftCardDTO;
import com.giftcard_app.poc_rest.models.GiftCard;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GiftCardMapper {
    GiftCardDTO toDTO(GiftCard giftCard);
    GiftCard toEntity(GiftCardDTO giftCardDTO);
}
