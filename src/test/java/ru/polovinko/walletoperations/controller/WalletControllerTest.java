package ru.polovinko.walletoperations.controller;

import org.apache.coyote.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.polovinko.walletoperations.dto.TransactionDTO;
import ru.polovinko.walletoperations.entity.OperationType;
import ru.polovinko.walletoperations.entity.Wallet;
import ru.polovinko.walletoperations.repository.WalletRepository;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class WalletControllerTest {
  @Container
  public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest")
    .withDatabaseName("wallet_db")
    .withUsername("postgres")
    .withPassword("3250325q");

  @DynamicPropertySource
  static void properties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
    registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
    registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
  }

  @Autowired
  private WalletRepository walletRepository;
  @Autowired
  private RestTemplateBuilder restTemplateBuilder;
  private RestTemplate restTemplate;
  private String baseUrl;

  @BeforeEach
  public void setup(@Value("${local.server.port}") int port) {
    this.baseUrl = "http://localhost:" + port + "/api/v1/wallet";
    this.restTemplate = restTemplateBuilder.build();
    walletRepository.deleteAll();
  }

  @AfterEach
  public void tearDown() {
    walletRepository.deleteAll();
  }

  @Test
  void testCreateWallet() {
    ResponseEntity<Wallet> response = restTemplate.postForEntity(baseUrl + "/create", null, Wallet.class);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(BigDecimal.ZERO, response.getBody().getBalance());
  }

  @Test
  void testHandleTransaction() {
    Wallet wallet = walletRepository.save(Wallet.builder().balance(BigDecimal.ZERO).build());
    TransactionDTO dto = TransactionDTO.builder()
      .walletId(wallet.getId())
      .operationType(OperationType.DEPOSIT)
      .amount(new BigDecimal("1000"))
      .build();
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<TransactionDTO> request = new HttpEntity<>(dto, headers);
    ResponseEntity<String> response = restTemplate.postForEntity(baseUrl, request, String.class);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("Транзакция прошла успешно", response.getBody());
    ResponseEntity<BigDecimal> balanceResponse = restTemplate.getForEntity(baseUrl + "/" + wallet.getId(), BigDecimal.class);
    assertEquals(HttpStatus.OK, balanceResponse.getStatusCode());
    assertEquals(0, BigDecimal.valueOf(1000).compareTo(balanceResponse.getBody()));
  }

  @Test
  void testGetWalletBalance() {
    Wallet wallet = walletRepository.save(Wallet.builder().balance(BigDecimal.valueOf(500)).build());
    ResponseEntity<BigDecimal> response = restTemplate.getForEntity(baseUrl + "/" + wallet.getId(), BigDecimal.class);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(0, BigDecimal.valueOf(500).compareTo(response.getBody()));
  }

  @Test
  void testHandleInsufficientFunds() {
    Wallet wallet = walletRepository.save(Wallet.builder().balance(BigDecimal.valueOf(500)).build());
    TransactionDTO dto = TransactionDTO.builder()
      .walletId(wallet.getId())
      .operationType(OperationType.WITHDRAW)
      .amount(new BigDecimal(1000))
      .build();
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<TransactionDTO> request = new HttpEntity<>(dto, headers);
    Exception exception = assertThrows(Exception.class, () -> {
      restTemplate.postForEntity(baseUrl, request, String.class);
    });
    String expectedMessage = "Недостаточно средств";
    String actualMessage = exception.getMessage();
    assertTrue(actualMessage.contains(expectedMessage));
  }
}
