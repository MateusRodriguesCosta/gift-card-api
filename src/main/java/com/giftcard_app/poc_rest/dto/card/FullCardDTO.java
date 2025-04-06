package com.giftcard_app.poc_rest.dto.card;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class FullCardDTO {
    @Schema(description = "Unique token for the gift card", example = "xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx")
    public String token;
    public String maskedCardNumber;
    public BigDecimal balance;
    public String currency;
    public String region;
    public String status;
    public String expiryDate;
    public String issueDate;

    /**
     * @return the last 4 digits of the card.
     */
    @Schema(example = "**** **** **** 1234")
    public String getMaskedCardNumber() {
        if(maskedCardNumber != null && maskedCardNumber.length() > 4) {
            return "**** **** **** " + maskedCardNumber.substring(maskedCardNumber.length() - 4);
        }
        return maskedCardNumber;
    }
}
