package com.costasolutions.giftcards.dto.transaction;

import com.costasolutions.giftcards.enums.TransactionType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class TransactionDTO {
    @NotNull
    private TransactionType transactionType;
    @NotNull
    private BigDecimal amount;
    @NotNull
    private LocalDateTime transactionDateTime;
    private UUID exchangeId;
    @NotNull
    private String giftCardToken;
}
