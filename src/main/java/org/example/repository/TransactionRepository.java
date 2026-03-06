package org.example.repository;

import org.example.model.Transaction;
import org.example.model.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByCarId(Long carId);

    boolean existsByCarIdAndType(Long carId, TransactionType type);

    @Query("SELECT COUNT(t) > 0 FROM Transaction t WHERE t.car.id = :carId AND t.type = org.example.model.TransactionType.RENTAL AND t.startDate < :endDate AND t.endDate > :startDate")
    boolean existsOverlappingRental(@Param("carId") Long carId,
                                    @Param("startDate") LocalDate startDate,
                                    @Param("endDate") LocalDate endDate);

    @Query("SELECT t FROM Transaction t WHERE t.car.id = :carId AND t.type = org.example.model.TransactionType.RENTAL")
    List<Transaction> findRentalsByCarId(@Param("carId") Long carId);
}
