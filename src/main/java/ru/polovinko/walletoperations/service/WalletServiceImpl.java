package ru.polovinko.walletoperations.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.polovinko.walletoperations.entity.OperationType;
import ru.polovinko.walletoperations.entity.Transaction;
import ru.polovinko.walletoperations.entity.Wallet;
import ru.polovinko.walletoperations.operation.Operation;
import ru.polovinko.walletoperations.operation.OperationFactory;
import ru.polovinko.walletoperations.repository.TransactionRepository;
import ru.polovinko.walletoperations.repository.WalletRepository;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {
  private final WalletRepository walletRepository;
  private final TransactionRepository transactionRepository;

  @Override
  @Transactional
  public void processTransaction(UUID walletId, OperationType operationType, BigDecimal amount) {
    Wallet wallet = walletRepository.findById(walletId)
      .orElseThrow(() -> new RuntimeException("Кошелек не найден!"));
    Operation operation = OperationFactory.getOperation(operationType);
    BigDecimal newBalance = operation.apply(wallet.getBalance(), amount);
    wallet.setBalance(newBalance);
    walletRepository.save(wallet);
    Transaction transaction = Transaction.builder()
      .id(UUID.randomUUID())
      .wallet(wallet)
      .amount(amount)
      .operationType(operationType)
      .timestamp(Instant.now())
      .build();
    transactionRepository.save(transaction);
  }

  @Override
  @Transactional(readOnly = true)
  public BigDecimal getWalletBalance(UUID walletId) {
    return walletRepository.findById(walletId)
      .orElseThrow(() -> new RuntimeException("Кошелек не найден!"))
      .getBalance();
  }
}
