package com.udea.lab1v2026.repository;

import com.udea.lab1v2026.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findBySenderAccountNumberOrReceiverAccountNumberOrderByTimestampDesc(
        String senderAccountNumber, String receiverAccountNumber);

    List<Transaction> findAllByOrderByTimestampDesc();
}
