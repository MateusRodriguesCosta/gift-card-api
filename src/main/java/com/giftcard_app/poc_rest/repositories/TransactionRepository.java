package com.giftcard_app.poc_rest.repositories;

import com.giftcard_app.poc_rest.models.GiftCard;
import com.giftcard_app.poc_rest.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
