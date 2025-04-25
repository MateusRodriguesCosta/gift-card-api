package com.costasolutions.giftcards.models;

import com.costasolutions.giftcards.enums.CardStatus;
import com.costasolutions.giftcards.validation.ValidGiftCardNumber;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GiftCard {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(unique = true, nullable = false)
    private String token;
    @Column(unique = true)
    @NotBlank(message = "Card number is mandatory")
    @ValidGiftCardNumber
    private String cardNumber;
    @NotNull(message = "Balance is mandatory")
    private BigDecimal balance;
    @NotNull(message = "Card currency is mandatory")
    private String currency;
    @NotNull(message = "Card region is mandatory")
    private String region;
    @NotNull(message = "Card status is mandatory")
    @Enumerated(EnumType.STRING)
    private CardStatus status;
    private LocalDate expiryDate;
    @NotNull(message = "Card issue date is mandatory")
    private LocalDateTime issueDate;
    @OneToMany(mappedBy = "giftCard", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> transactions = new ArrayList<>();
}
