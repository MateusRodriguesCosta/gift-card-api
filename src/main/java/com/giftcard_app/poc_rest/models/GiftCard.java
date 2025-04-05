package com.giftcard_app.poc_rest.models;

import java.math.BigDecimal;
import java.util.UUID;

import com.giftcard_app.poc_rest.validation.ValidGiftCardNumber;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GiftCard {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public UUID id;
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
    public String status;
    public String expiryDate;
    @NotNull(message = "Card issue date is mandatory")
    public String issueDate;
    public String user_id;
}
