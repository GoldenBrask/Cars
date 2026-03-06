package org.example.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
@Table(name = "cars")
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Car type is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CarType type;

    @NotBlank(message = "Brand is required")
    @Column(nullable = false)
    private String brand;

    @NotBlank(message = "Model is required")
    @Column(nullable = false)
    private String model;

    @NotNull(message = "Purchase date is required")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "purchase_date", nullable = false)
    private LocalDate purchaseDate;

    @NotNull(message = "Car price is required")
    @Positive(message = "Car price must be greater than 0")
    @Column(name = "car_price", nullable = false)
    private Double carPrice;

    @NotNull(message = "Rental price per day is required")
    @Positive(message = "Rental price per day must be greater than 0")
    @Column(name = "rental_price_per_day", nullable = false)
    private Double rentalPricePerDay;

    @NotNull(message = "Max passengers is required")
    @Positive(message = "Max passengers must be greater than 0")
    @Column(name = "max_passengers", nullable = false)
    private Integer maxPassengers;

    @NotNull(message = "Max speed is required")
    @Positive(message = "Max speed must be greater than 0")
    @Column(name = "max_speed", nullable = false)
    private Integer maxSpeed;

    @Column(name = "air_conditioner", nullable = false)
    private boolean airConditioner;

    @Column(name = "automatic_transmission", nullable = false)
    private boolean automaticTransmission;

    public Car() {}

    public Car(CarType type, String brand, String model, LocalDate purchaseDate,
               Double carPrice, Double rentalPricePerDay,
               Integer maxPassengers, Integer maxSpeed,
               boolean airConditioner, boolean automaticTransmission) {
        this.type = type;
        this.brand = brand;
        this.model = model;
        this.purchaseDate = purchaseDate;
        this.carPrice = carPrice;
        this.rentalPricePerDay = rentalPricePerDay;
        this.maxPassengers = maxPassengers;
        this.maxSpeed = maxSpeed;
        this.airConditioner = airConditioner;
        this.automaticTransmission = automaticTransmission;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public CarType getType() { return type; }
    public void setType(CarType type) { this.type = type; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public LocalDate getPurchaseDate() { return purchaseDate; }
    public void setPurchaseDate(LocalDate purchaseDate) { this.purchaseDate = purchaseDate; }

    public Double getCarPrice() { return carPrice; }
    public void setCarPrice(Double carPrice) { this.carPrice = carPrice; }

    public Double getRentalPricePerDay() { return rentalPricePerDay; }
    public void setRentalPricePerDay(Double rentalPricePerDay) { this.rentalPricePerDay = rentalPricePerDay; }

    public Integer getMaxPassengers() { return maxPassengers; }
    public void setMaxPassengers(Integer maxPassengers) { this.maxPassengers = maxPassengers; }

    public Integer getMaxSpeed() { return maxSpeed; }
    public void setMaxSpeed(Integer maxSpeed) { this.maxSpeed = maxSpeed; }

    public boolean isAirConditioner() { return airConditioner; }
    public void setAirConditioner(boolean airConditioner) { this.airConditioner = airConditioner; }

    public boolean isAutomaticTransmission() { return automaticTransmission; }
    public void setAutomaticTransmission(boolean automaticTransmission) { this.automaticTransmission = automaticTransmission; }

    @Override
    public String toString() {
        return "Car{id=" + id + ", type=" + type + ", brand='" + brand + "', model='" + model + "'}";
    }
}
