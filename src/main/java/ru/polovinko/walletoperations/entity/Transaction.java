package ru.polovinko.walletoperations.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Transaction {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;
  @ManyToOne
  @JoinColumn(name = "wallet_id", nullable = false)
  private Wallet wallet;
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private OperationType operationType;
  @Column(nullable = false)
  private BigDecimal amount;
  @Column(nullable = false)
  private Instant timestamp;
}
