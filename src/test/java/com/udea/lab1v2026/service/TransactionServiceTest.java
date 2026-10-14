package com.udea.lab1v2026.service;

import com.udea.lab1v2026.dto.TransactionDTO;
import com.udea.lab1v2026.dto.TransferRequestDTO;
import com.udea.lab1v2026.entity.Customer;
import com.udea.lab1v2026.entity.Transaction;
import com.udea.lab1v2026.repository.CustomerRepository;
import com.udea.lab1v2026.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {
    @Mock private TransactionRepository transactionRepository;
    @Mock private CustomerRepository customerRepository;

    private TransactionService service;

    @BeforeEach
    void setUp() {
        service = new TransactionService(transactionRepository, customerRepository);
    }

    @Test
    void getAllTransactionsReturnsDtosInRepositoryOrder() {
        Transaction tx = transaction(1L, "1001", "2002", 25.0);
        when(transactionRepository.findAllByOrderByTimestampDesc()).thenReturn(List.of(tx));

        List<TransactionDTO> result = service.getAllTransactions();

        assertEquals(1, result.size());
        assertEquals(25.0, result.get(0).getAmount());
    }

    @Test
    void getTransactionsForAccountReturnsHistory() {
        Transaction tx = transaction(1L, "1001", "2002", 25.0);
        when(transactionRepository.findBySenderAccountNumberOrReceiverAccountNumberOrderByTimestampDesc("1001", "1001"))
                .thenReturn(List.of(tx));

        List<TransactionDTO> result = service.getTransactionsForAccount("1001");

        assertEquals("2002", result.get(0).getReceiverAccountNumber());
    }

    @Test
    void transferMoneyMovesBalanceAndCreatesTransaction() {
        Customer sender = customer(1L, "1001", 100.0);
        Customer receiver = customer(2L, "2002", 20.0);
        TransferRequestDTO request = request("1001", "2002", 30.0);

        when(customerRepository.findByAccountNumber("1001")).thenReturn(Optional.of(sender));
        when(customerRepository.findByAccountNumber("2002")).thenReturn(Optional.of(receiver));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(invocation -> {
            Transaction tx = invocation.getArgument(0);
            tx.setId(10L);
            tx.setTimestamp(LocalDateTime.of(2026, 10, 13, 12, 0));
            return tx;
        });

        TransactionDTO result = service.transferMoney(request);

        assertEquals(70.0, sender.getBalance());
        assertEquals(50.0, receiver.getBalance());
        assertEquals(30.0, result.getAmount());
        verify(customerRepository).save(sender);
        verify(customerRepository).save(receiver);
        verify(transactionRepository).save(any(Transaction.class));
    }

    @Test
    void transferRejectsSameAccount() {
        TransferRequestDTO request = request("1001", "1001", 10.0);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.transferMoney(request));
        assertEquals("La cuenta origen y destino deben ser diferentes", ex.getMessage());
    }

    @Test
    void transferRejectsMissingSender() {
        when(customerRepository.findByAccountNumber("1001")).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class,
                () -> service.transferMoney(request("1001", "2002", 10.0)));
    }

    @Test
    void transferRejectsMissingReceiver() {
        when(customerRepository.findByAccountNumber("1001")).thenReturn(Optional.of(customer(1L, "1001", 100.0)));
        when(customerRepository.findByAccountNumber("2002")).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class,
                () -> service.transferMoney(request("1001", "2002", 10.0)));
    }

    @Test
    void transferRejectsInsufficientBalance() {
        when(customerRepository.findByAccountNumber("1001")).thenReturn(Optional.of(customer(1L, "1001", 5.0)));
        when(customerRepository.findByAccountNumber("2002")).thenReturn(Optional.of(customer(2L, "2002", 0.0)));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.transferMoney(request("1001", "2002", 10.0)));

        assertEquals("Saldo insuficiente", ex.getMessage());
        verify(transactionRepository, never()).save(any());
    }

    private TransferRequestDTO request(String sender, String receiver, double amount) {
        TransferRequestDTO dto = new TransferRequestDTO();
        dto.setSenderAccountNumber(sender);
        dto.setReceiverAccountNumber(receiver);
        dto.setAmount(amount);
        return dto;
    }

    private Customer customer(Long id, String account, Double balance) {
        Customer c = new Customer();
        c.setId(id);
        c.setFirstName("Nombre");
        c.setLastName("Apellido");
        c.setAccountNumber(account);
        c.setBalance(balance);
        return c;
    }

    private Transaction transaction(Long id, String sender, String receiver, Double amount) {
        Transaction tx = new Transaction();
        tx.setId(id);
        tx.setSenderAccountNumber(sender);
        tx.setReceiverAccountNumber(receiver);
        tx.setAmount(amount);
        tx.setTimestamp(LocalDateTime.of(2026, 10, 13, 12, 0));
        return tx;
    }
}
