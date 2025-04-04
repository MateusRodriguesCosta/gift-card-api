package com.giftcard_app.poc_rest.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class GiftCardDTO {

    public UUID id;
    public String cardNumber;
    public BigDecimal balance;
    public String cardStatus;
    public String cardExpiryDate;
    public String cardIssueDate;
}
