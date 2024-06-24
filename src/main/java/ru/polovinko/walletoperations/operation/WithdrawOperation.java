package ru.polovinko.walletoperations.operation;

import java.math.BigDecimal;

public class WithdrawOperation implements Operation {
  @Override
  public BigDecimal apply(BigDecimal balance, BigDecimal amount) {
    if (amount.compareTo(BigDecimal.ZERO) <= 0) {
      throw new RuntimeException("Сумма должна быть положительной");
    }
    if (balance.compareTo(amount) < 0) {
      throw new RuntimeException("Недостаточно средств");
    }
    return balance.subtract(amount);
  }
}
