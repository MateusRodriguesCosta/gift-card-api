package com.giftcard_app.poc_rest.dto.card;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class CreateCardDTO {
    public BigDecimal balance;
    public String currency;
    public String region;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    public LocalDate expiryDate;
}
