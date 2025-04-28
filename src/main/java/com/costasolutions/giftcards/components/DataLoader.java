package com.costasolutions.giftcards.components;

import com.costasolutions.giftcards.enums.CardStatus;
import com.costasolutions.giftcards.models.GiftCard;
import com.costasolutions.giftcards.models.User;
import com.costasolutions.giftcards.repositories.GiftCardRepository;
import com.costasolutions.giftcards.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepo;
    private final GiftCardRepository giftRepo;
    private final PasswordEncoder encoder;
    private final CardTokenGenerator cardTokenGenerator;
    private final CardNumberGenerator cardNumberGenerator;

    public DataLoader(UserRepository userRepo,
                      GiftCardRepository giftRepo,
                      PasswordEncoder encoder,
                      CardTokenGenerator cardTokenGenerator,
                      CardNumberGenerator cardNumberGenerator) {
        this.userRepo = userRepo;
        this.giftRepo = giftRepo;
        this.encoder = encoder;
        this.cardTokenGenerator = cardTokenGenerator;
        this.cardNumberGenerator = cardNumberGenerator;
    }

    @Override
    public void run(String... args) {
        if (userRepo.count() == 0) {
            userRepo.saveAll(List.of(
                    User.builder()
                            .username("user")
                            .password(encoder.encode("user123"))
                            .roles("ROLE_USER")
                            .build()
            ));
        }
        if (giftRepo.count() == 0) {
            for (int i = 0; i < 30; i++) {

                CardStatus[] statuses = new CardStatus[]{
                        CardStatus.ACTIVE,
                        CardStatus.CANCELLED,
                        CardStatus.EXPIRED
                };
                CardStatus randomStatus = statuses[
                        ThreadLocalRandom.current().nextInt(statuses.length)
                        ];
                double randomDouble = ThreadLocalRandom.current().nextDouble(0, 2000);
                BigDecimal randomBalance = BigDecimal.valueOf(randomDouble)
                        .setScale(2, RoundingMode.HALF_UP);
                giftRepo.save(GiftCard.builder()
                        .token(cardTokenGenerator.generateToken())
                        .cardNumber(cardNumberGenerator.generateCardNumber())
                        .issueDate(LocalDateTime.now().minusDays((long) randomDouble))
                        .expiryDate(randomStatus == CardStatus.EXPIRED ?
                                LocalDate.now().minusDays(5)
                                : LocalDate.now().plusDays((long) randomDouble))
                        .balance(randomBalance)
                        .status(randomStatus)
                        .region("US")
                        .currency("USD")
                        .build());
            }
        }
    }
}