package org.example.service;

import org.example.model.Transaction;
import org.example.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    public List<Transaction> findAll() {
        return transactionRepository.findAll();
    }

    public Transaction save(Transaction transaction) {
        return transactionRepository.save(transaction);
    }
}
