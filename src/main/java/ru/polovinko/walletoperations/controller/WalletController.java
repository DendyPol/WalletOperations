package ru.polovinko.walletoperations.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.polovinko.walletoperations.dto.TransactionDTO;
import ru.polovinko.walletoperations.entity.Wallet;
import ru.polovinko.walletoperations.repository.WalletRepository;
import ru.polovinko.walletoperations.service.WalletService;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/wallet")
@RequiredArgsConstructor
public class WalletController {
  private final WalletService walletService;
  private final WalletRepository walletRepository;

  @PostMapping("/create")
  public ResponseEntity<Wallet> createWallet() {
    Wallet wallet = Wallet.builder()
      .balance(BigDecimal.ZERO)
      .build();
    Wallet createdWallet = walletRepository.save(wallet);
    return ResponseEntity.ok(createdWallet);
  }

  @PostMapping
  public ResponseEntity<String> handleTransaction(@RequestBody TransactionDTO dto) {
    walletService.processTransaction(dto.getWalletId(), dto.getOperationType(), dto.getAmount());
    return ResponseEntity.ok("Транзакция прошла успешно");
  }

  @GetMapping("/{walletId}")
  public ResponseEntity<BigDecimal> getWalletBalance(@PathVariable UUID walletId) {
    return ResponseEntity.ok(walletService.getWalletBalance(walletId));
  }
}
