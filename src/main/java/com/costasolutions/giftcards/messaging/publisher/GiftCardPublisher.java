package com.costasolutions.giftcards.messaging.publisher;

import com.costasolutions.giftcards.enums.TransactionType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.spring.pubsub.core.PubSubTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class GiftCardPublisher {
    private final PubSubTemplate pubSubTemplate;
    private final ObjectMapper objectMapper;
    public record GiftCardEvent(
            String giftCardToken,
            TransactionType transactionType,
            BigDecimal amount,
            LocalDateTime timestamp
    ) {
    }

    @Autowired
    public GiftCardPublisher(PubSubTemplate pubSubTemplate, ObjectMapper objectMapper) {
        this.pubSubTemplate = pubSubTemplate;
        this.objectMapper = objectMapper;
    }

    public void publishEvent(GiftCardEvent event) {
        try {
            String json = objectMapper.writeValueAsString(event);
            pubSubTemplate.publish("gift-card-topic", json);

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize event to JSON", e);
        }
    }
}
