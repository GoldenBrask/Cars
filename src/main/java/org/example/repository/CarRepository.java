package org.example.repository;

import org.example.model.Car;
import org.example.model.CarType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {

    List<Car> findByBrandContainingIgnoreCase(String brand);

    List<Car> findByType(CarType type);
}
