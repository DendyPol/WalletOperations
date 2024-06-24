package ru.polovinko.walletoperations.operation;

import java.math.BigDecimal;

public class DepositOperation implements Operation {
  @Override
  public BigDecimal apply(BigDecimal balance, BigDecimal amount) {
    if (amount.compareTo(BigDecimal.ZERO) <= 0) {
      throw new RuntimeException("Сумма должна быть положительной");
    }
    return balance.add(amount);
  }
}
