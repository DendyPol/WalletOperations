package ru.polovinko.walletoperations.operation;

import java.math.BigDecimal;

public interface Operation {
  BigDecimal apply(BigDecimal balance, BigDecimal amount);
}
