package org.example.service;

import org.example.model.Transaction;
import org.example.model.TransactionType;
import org.example.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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

    public boolean isCarSold(Long carId) {
        return transactionRepository.existsByCarIdAndType(carId, TransactionType.PURCHASE);
    }

    public boolean isCarAvailableForRent(Long carId, LocalDate startDate, LocalDate endDate) {
        if (isCarSold(carId)) return false;
        return !transactionRepository.existsOverlappingRental(carId, startDate, endDate);
    }

    public List<Transaction> findRentalsByCarId(Long carId) {
        return transactionRepository.findRentalsByCarId(carId);
    }
}
