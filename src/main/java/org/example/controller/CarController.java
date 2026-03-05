package org.example.controller;

import jakarta.validation.Valid;
import org.example.model.Car;
import org.example.model.CarType;
import org.example.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/cars")
public class CarController {

    @Autowired
    private CarService carService;

    // List all cars with optional search by brand
    @GetMapping
    public String listCars(Model model,
                           @RequestParam(required = false) String search) {
        List<Car> cars;
        if (search != null && !search.isBlank()) {
            cars = carService.findByBrand(search);
            model.addAttribute("search", search);
        } else {
            cars = carService.findAll();
        }
        model.addAttribute("cars", cars);
        model.addAttribute("carTypes", CarType.values());
        return "cars/list";
    }

    // Show create form
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("car", new Car());
        model.addAttribute("carTypes", CarType.values());
        model.addAttribute("pageTitle", "Add a Car");
        return "cars/form";
    }

    // Save new car
    @PostMapping
    public String createCar(@Valid @ModelAttribute Car car, BindingResult result,
                            Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("carTypes", CarType.values());
            model.addAttribute("pageTitle", "Add a Car");
            return "cars/form";
        }
        carService.save(car);
        redirectAttributes.addFlashAttribute("successMessage", "Car added successfully!");
        return "redirect:/cars";
    }

    // Show edit form
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Car> car = carService.findById(id);
        if (car.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Car not found.");
            return "redirect:/cars";
        }
        model.addAttribute("car", car.get());
        model.addAttribute("carTypes", CarType.values());
        model.addAttribute("pageTitle", "Edit Car");
        return "cars/form";
    }

    // Update car
    @PostMapping("/edit/{id}")
    public String updateCar(@PathVariable Long id, @Valid @ModelAttribute Car car,
                            BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("carTypes", CarType.values());
            model.addAttribute("pageTitle", "Edit Car");
            return "cars/form";
        }
        car.setId(id);
        carService.save(car);
        redirectAttributes.addFlashAttribute("successMessage", "Car updated successfully!");
        return "redirect:/cars";
    }

    // Delete car
    @PostMapping("/delete/{id}")
    public String deleteCar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        if (!carService.existsById(id)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Car not found.");
        } else {
            carService.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Car deleted successfully!");
        }
        return "redirect:/cars";
    }

    // View car detail
    @GetMapping("/{id}")
    public String viewCar(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Car> car = carService.findById(id);
        if (car.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Car not found.");
            return "redirect:/cars";
        }
        model.addAttribute("car", car.get());
        return "cars/detail";
    }
}
