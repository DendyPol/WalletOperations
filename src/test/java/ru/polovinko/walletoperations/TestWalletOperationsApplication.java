package ru.polovinko.walletoperations;

import org.springframework.boot.SpringApplication;

public class TestWalletOperationsApplication {

  public static void main(String[] args) {
    SpringApplication.from(WalletOperationsApplication::main).with(TestcontainersConfiguration.class).run(args);
  }

}
