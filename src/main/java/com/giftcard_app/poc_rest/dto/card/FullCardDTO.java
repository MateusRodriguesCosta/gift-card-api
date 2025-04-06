package com.giftcard_app.poc_rest.dto.card;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class FullCardDTO {
    public UUID id;
    public String token;
    public String maskedCardNumber;
    public BigDecimal balance;
    public String currency;
    public String region;
    public String status;
    public String expiryDate;
    public String issueDate;

    public String getMaskedCardNumber() {
        if(maskedCardNumber != null && maskedCardNumber.length() > 4) {
            return "**** **** **** " + maskedCardNumber.substring(maskedCardNumber.length() - 4);
        }
        return maskedCardNumber;
    }
}
