package com.costasolutions.giftcards.repositories;

import com.costasolutions.giftcards.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
