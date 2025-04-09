package com.giftcard_app.poc_rest.mapper;

import com.giftcard_app.poc_rest.dto.card.CreateCardDTO;
import com.giftcard_app.poc_rest.dto.card.FullCardDTO;
import com.giftcard_app.poc_rest.dto.transaction.TransactionDTO;
import com.giftcard_app.poc_rest.models.GiftCard;
import com.giftcard_app.poc_rest.models.Transaction;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TransactionMapper {
    TransactionDTO toCreateDTO(Transaction transaction);
    Transaction toEntity(TransactionDTO transactionDTO);
}
