package ru.polovinko.walletoperations.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import ru.polovinko.walletoperations.util.OperationTypeDeserializer;

@JsonDeserialize(using = OperationTypeDeserializer.class)
public enum OperationType {
  DEPOSIT,
  WITHDRAW
}
