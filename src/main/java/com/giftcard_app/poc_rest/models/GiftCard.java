package com.giftcard_app.poc_rest.models;

import com.giftcard_app.poc_rest.enums.CardStatus;
import com.giftcard_app.poc_rest.validation.ValidGiftCardNumber;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GiftCard {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public UUID id;
    @Column(unique = true, nullable = false)
    public String token;
    @Column(unique = true)
    @NotBlank(message = "Card number is mandatory")
    @ValidGiftCardNumber
    public String cardNumber;
    @NotNull(message = "Balance is mandatory")
    public BigDecimal balance;
    @NotNull(message = "Card currency is mandatory")
    public String currency;
    @NotNull(message = "Card region is mandatory")
    public String region;
    @NotNull(message = "Card status is mandatory")
    @Enumerated(EnumType.STRING)
    public CardStatus status;
    public LocalDate expiryDate;
    @NotNull(message = "Card issue date is mandatory")
    public LocalDateTime issueDate;
    public String user_id;
}
