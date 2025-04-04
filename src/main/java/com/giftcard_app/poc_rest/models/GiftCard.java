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
    @NotNull(message = "Card Status is mandatory")
    public String cardStatus;
    public String cardExpiryDate;
    @NotNull(message = "Card Issue Date is mandatory")
    public String cardIssueDate;
    public String user_id;
}
