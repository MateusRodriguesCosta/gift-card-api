package com.giftcard_app.poc_rest.models;

import com.giftcard_app.poc_rest.enums.TransactionType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public UUID id;
    @NotNull
    @Enumerated(EnumType.STRING)
    public TransactionType transactionType;
    @NotNull
    public BigDecimal transactionAmount;
    @NotNull
    public LocalDateTime transactionDateTime;
    private UUID exchangeId;
    @ManyToOne
    @JoinColumn(name = "gift_card_id", nullable = false)
    private GiftCard giftCard;
}
