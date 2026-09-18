package com.udea.lab1v2026.service;

import com.udea.lab1v2026.dto.TransactionDTO;
import com.udea.lab1v2026.dto.TransferRequestDTO;
import com.udea.lab1v2026.entity.Customer;
import com.udea.lab1v2026.entity.Transaction;
import com.udea.lab1v2026.repository.CustomerRepository;
import com.udea.lab1v2026.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final CustomerRepository customerRepository;

    public TransactionService(TransactionRepository transactionRepository, CustomerRepository customerRepository) {
        this.transactionRepository = transactionRepository;
        this.customerRepository = customerRepository;
    }

    public List<TransactionDTO> getAllTransactions() {
        return transactionRepository.findAllByOrderByTimestampDesc().stream().map(this::toDTO).toList();
    }

    public List<TransactionDTO> getTransactionsForAccount(String accountNumber) {
        return transactionRepository
                .findBySenderAccountNumberOrReceiverAccountNumberOrderByTimestampDesc(accountNumber, accountNumber)
                .stream().map(this::toDTO).toList();
    }

    @Transactional
    public TransactionDTO transferMoney(TransferRequestDTO request) {
        String senderNumber = request.getSenderAccountNumber().trim();
        String receiverNumber = request.getReceiverAccountNumber().trim();

        if (senderNumber.equals(receiverNumber)) {
            throw new IllegalArgumentException("La cuenta origen y destino deben ser diferentes");
        }

        Customer sender = customerRepository.findByAccountNumber(senderNumber)
                .orElseThrow(() -> new IllegalArgumentException("Cuenta origen no encontrada"));
        Customer receiver = customerRepository.findByAccountNumber(receiverNumber)
                .orElseThrow(() -> new IllegalArgumentException("Cuenta destino no encontrada"));

        if (sender.getBalance() < request.getAmount()) {
            throw new IllegalArgumentException("Saldo insuficiente");
        }

        sender.setBalance(sender.getBalance() - request.getAmount());
        receiver.setBalance(receiver.getBalance() + request.getAmount());
        customerRepository.save(sender);
        customerRepository.save(receiver);

        Transaction transaction = new Transaction();
        transaction.setSenderAccountNumber(senderNumber);
        transaction.setReceiverAccountNumber(receiverNumber);
        transaction.setAmount(request.getAmount());
        return toDTO(transactionRepository.save(transaction));
    }

    private TransactionDTO toDTO(Transaction t) {
        return new TransactionDTO(t.getId(), t.getSenderAccountNumber(), t.getReceiverAccountNumber(), t.getAmount(), t.getTimestamp());
    }
}
