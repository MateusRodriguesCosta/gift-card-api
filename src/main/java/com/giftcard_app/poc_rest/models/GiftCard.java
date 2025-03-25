package com.giftcard_app.poc_rest.models;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public String id;
    @Column(unique = true, nullable = false)
    @NotBlank(message = "Code is mandatory")
    public String code;    
    @NotNull(message = "Balance is mandatory")
    public BigDecimal balance;
    @NotNull(message = "Card Status is mandatory")
    public String cardStatus;    
    public String cardExpiryDate;
    @NotNull(message = "Card Issue Date is mandatory")
    public String cardIssueDate;
    public String user_id;
}
