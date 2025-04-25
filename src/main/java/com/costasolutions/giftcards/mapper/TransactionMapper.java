package com.costasolutions.giftcards.mapper;

import com.costasolutions.giftcards.dto.transaction.TransactionDTO;
import com.costasolutions.giftcards.models.Transaction;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TransactionMapper {
    TransactionDTO toCreateDTO(Transaction transaction);
    Transaction toEntity(TransactionDTO transactionDTO);
}
