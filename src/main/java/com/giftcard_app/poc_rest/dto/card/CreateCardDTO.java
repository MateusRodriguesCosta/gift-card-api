package com.giftcard_app.poc_rest.dto.card;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateCardDTO {
    public BigDecimal balance;
    public String currency;
    public String region;
    public String expiryDate;
}
