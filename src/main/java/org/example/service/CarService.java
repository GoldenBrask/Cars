package org.example.service;

import org.example.model.Car;
import org.example.model.CarType;
import org.example.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CarService {

    @Autowired
    private CarRepository carRepository;

    public List<Car> findAll() {
        return carRepository.findAll();
    }

    public Optional<Car> findById(Long id) {
        return carRepository.findById(id);
    }

    public Car save(Car car) {
        return carRepository.save(car);
    }

    public void deleteById(Long id) {
        carRepository.deleteById(id);
    }

    public List<Car> findByBrand(String brand) {
        return carRepository.findByBrandContainingIgnoreCase(brand);
    }

    public List<Car> findByType(CarType type) {
        return carRepository.findByType(type);
    }

    public boolean existsById(Long id) {
        return carRepository.existsById(id);
    }
}
