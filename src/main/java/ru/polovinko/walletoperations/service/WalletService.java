package ru.polovinko.walletoperations.service;

import ru.polovinko.walletoperations.entity.OperationType;

import java.math.BigDecimal;
import java.util.UUID;

public interface WalletService {
  void processTransaction(UUID walletId, OperationType operationType, BigDecimal amount);
  BigDecimal getWalletBalance(UUID walletId);
}
