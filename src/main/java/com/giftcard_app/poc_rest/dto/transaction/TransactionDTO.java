package com.giftcard_app.poc_rest.dto.transaction;

import com.giftcard_app.poc_rest.enums.TransactionType;
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
