package ru.polovinko.walletoperations.util;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import ru.polovinko.walletoperations.entity.OperationType;

import java.io.IOException;

public class OperationTypeDeserializer extends JsonDeserializer<OperationType> {
  @Override
  public OperationType deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
    String key = p.getText();
    for (OperationType type : OperationType.values()) {
      if (type.name().equalsIgnoreCase(key)) {
        return type;
      }
    }
    throw new IllegalArgumentException();
  }
}
