package com.giftcard_app.poc_rest.elastic;

import com.giftcard_app.poc_rest.enums.TransactionType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@Document(indexName = "transactions")
public class TransactionDocument {

    @Id
    private UUID id;
    private TransactionType transactionType;
    private BigDecimal transactionAmount;
    private LocalDateTime transactionDateTime;
    private UUID exchangeId;
    private String giftCardToken;
}
