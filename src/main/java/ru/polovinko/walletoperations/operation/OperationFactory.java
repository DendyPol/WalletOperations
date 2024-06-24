package ru.polovinko.walletoperations.operation;

import ru.polovinko.walletoperations.entity.OperationType;

public class OperationFactory {
  public static Operation getOperation(OperationType operationType) {
    return switch (operationType) {
      case DEPOSIT -> new DepositOperation();
      case WITHDRAW -> new WithdrawOperation();
      default -> throw new RuntimeException("Неверный тип операции");
    };
  }
}
