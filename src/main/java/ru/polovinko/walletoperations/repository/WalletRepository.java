package ru.polovinko.walletoperations.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.polovinko.walletoperations.entity.Wallet;

import java.util.UUID;

public interface WalletRepository extends JpaRepository<Wallet, UUID> {
}
