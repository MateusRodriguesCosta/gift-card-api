package com.giftcard_app.poc_rest.dto.card;

import com.giftcard_app.poc_rest.enums.CardStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class FullCardDTO {
    @Schema(description = "Unique token for the gift card", example = "xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx")
    private String token;
    private String maskedCardNumber;
    private BigDecimal balance;
    private String currency;
    private String region;
    private CardStatus status;
    private LocalDate expiryDate;
    private LocalDateTime issueDate;

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
