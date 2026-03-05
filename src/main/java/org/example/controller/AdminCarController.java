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

import java.util.Optional;

@Controller
@RequestMapping("/admin/cars")
public class AdminCarController {

    @Autowired
    private CarService carService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("cars", carService.findAll());
        return "admin/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("car", new Car());
        model.addAttribute("carTypes", CarType.values());
        model.addAttribute("pageTitle", "Add Car");
        return "admin/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute Car car, BindingResult result,
                         Model model, RedirectAttributes ra) {
        if (result.hasErrors()) {
            model.addAttribute("carTypes", CarType.values());
            model.addAttribute("pageTitle", "Add Car");
            return "admin/form";
        }
        carService.save(car);
        ra.addFlashAttribute("successMessage", "Car added successfully!");
        return "redirect:/admin/cars";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes ra) {
        Optional<Car> car = carService.findById(id);
        if (car.isEmpty()) { ra.addFlashAttribute("errorMessage", "Car not found."); return "redirect:/admin/cars"; }
        model.addAttribute("car", car.get());
        model.addAttribute("carTypes", CarType.values());
        model.addAttribute("pageTitle", "Edit Car");
        return "admin/form";
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable Long id, @Valid @ModelAttribute Car car,
                         BindingResult result, Model model, RedirectAttributes ra) {
        if (result.hasErrors()) {
            model.addAttribute("carTypes", CarType.values());
            model.addAttribute("pageTitle", "Edit Car");
            return "admin/form";
        }
        car.setId(id);
        carService.save(car);
        ra.addFlashAttribute("successMessage", "Car updated successfully!");
        return "redirect:/admin/cars";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        if (!carService.existsById(id)) {
            ra.addFlashAttribute("errorMessage", "Car not found.");
        } else {
            carService.deleteById(id);
            ra.addFlashAttribute("successMessage", "Car deleted successfully!");
        }
        return "redirect:/admin/cars";
    }
}
