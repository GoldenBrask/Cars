package org.example.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "car_id", nullable = false)
    private Car car;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type; // RENTAL or PURCHASE

    @Column(name = "customer_name", nullable = false)
    private String customerName;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    // only for RENTAL: end date
    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "total_price", nullable = false)
    private Double totalPrice;

    public Transaction() {}

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Car getCar() { return car; }
    public void setCar(Car car) { this.car = car; }

    public TransactionType getType() { return type; }
    public void setType(TransactionType type) { this.type = type; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public Double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(Double totalPrice) { this.totalPrice = totalPrice; }
}
