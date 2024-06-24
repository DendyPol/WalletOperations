package ru.polovinko.walletoperations.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.polovinko.walletoperations.entity.Transaction;

import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
}
