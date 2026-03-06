package org.example.controller;

import org.example.model.*;
import org.example.service.CarService;
import org.example.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/shop")
public class ShopController {

    @Autowired
    private CarService carService;

    @Autowired
    private TransactionService transactionService;

    @GetMapping
    public String catalog(Model model, @RequestParam(required = false) String search) {
        List<Car> allCars;

        if (search != null && !search.isBlank()) {
            allCars = carService.findByBrand(search);
        } else {
            allCars = carService.findAll();
        }

        List<Car> availableCars = new ArrayList<>();

        for (Car c : allCars) {
            if (!transactionService.isCarSold(c.getId())) {
                availableCars.add(c);
            }
        }

        model.addAttribute("cars", availableCars);
        model.addAttribute("search", search);
        return "shop/catalog";
    }

    @GetMapping("/rent/{id}")
    public String showRentForm(@PathVariable Long id, Model model, RedirectAttributes ra) {
        Optional<Car> car = carService.findById(id);
        if (car.isEmpty()) {
            ra.addFlashAttribute("errorMessage", "Véhicule introuvable.");
            return "redirect:/shop";
        }
        if (transactionService.isCarSold(id)) {
            ra.addFlashAttribute("errorMessage", "Ce véhicule a déjà été vendu et n'est plus disponible.");
            return "redirect:/shop";
        }


        List<Transaction> rentals = transactionService.findRentalsByCarId(id);
        StringBuilder periodesJson = new StringBuilder("[");
        for (int i = 0; i < rentals.size(); i++) {
            Transaction r = rentals.get(i);
            periodesJson.append("{\"start\":\"").append(r.getStartDate()).append("\",")
                        .append("\"end\":\"").append(r.getEndDate()).append("\"}");
            if (i < rentals.size() - 1) periodesJson.append(",");
        }
        periodesJson.append("]");

        model.addAttribute("car", car.get());
        model.addAttribute("periodesReservees", periodesJson.toString());
        return "shop/rent";
    }

    @PostMapping("/rent/{id}")
    public String processRent(@PathVariable Long id,
                              @RequestParam String startDate,
                              @RequestParam String endDate,
                              Principal principal,
                              RedirectAttributes ra) {
        Optional<Car> carOpt = carService.findById(id);
        if (carOpt.isEmpty()) {
            ra.addFlashAttribute("errorMessage", "Car not found.");
            return "redirect:/shop";
        }

        Car car = carOpt.get();
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        long days = ChronoUnit.DAYS.between(start, end);

        if (days <= 0) {
            ra.addFlashAttribute("errorMessage", "La date de fin doit être postérieure à la date de début.");
            return "redirect:/shop/rent/" + id;
        }

        if (!transactionService.isCarAvailableForRent(id, start, end)) {
            ra.addFlashAttribute("errorMessage", "Ce véhicule n'est pas disponible pour les dates sélectionnées.");
            return "redirect:/shop/rent/" + id;
        }

        Transaction t = new Transaction();
        t.setCar(car);
        t.setType(TransactionType.RENTAL);
        t.setCustomerName(principal.getName());
        t.setStartDate(start);
        t.setEndDate(end);
        t.setTotalPrice(car.getRentalPricePerDay() * days);
        transactionService.save(t);

        ra.addFlashAttribute("successMessage",
                "Location confirmée pour " + days + " jour(s) ! Total : " + t.getTotalPrice() + " $");
        return "redirect:/shop";
    }

    @GetMapping("/buy/{id}")
    public String showBuyForm(@PathVariable Long id, Model model, RedirectAttributes ra) {
        Optional<Car> car = carService.findById(id);
        if (car.isEmpty()) {
            ra.addFlashAttribute("errorMessage", "Car not found.");
            return "redirect:/shop";
        }
        if (transactionService.isCarSold(id)) {
            ra.addFlashAttribute("errorMessage", "Ce véhicule a déjà été vendu.");
            return "redirect:/shop";
        }
        model.addAttribute("car", car.get());
        return "shop/buy";
    }

    @PostMapping("/buy/{id}")
    public String processBuy(@PathVariable Long id,
                             Principal principal,
                             RedirectAttributes ra) {
        Optional<Car> carOpt = carService.findById(id);
        if (carOpt.isEmpty()) {
            ra.addFlashAttribute("errorMessage", "Car not found.");
            return "redirect:/shop";
        }
        if (transactionService.isCarSold(id)) {
            ra.addFlashAttribute("errorMessage", "Ce véhicule a déjà été vendu.");
            return "redirect:/shop";
        }

        Car car = carOpt.get();
        Transaction t = new Transaction();
        t.setCar(car);
        t.setType(TransactionType.PURCHASE);
        t.setCustomerName(principal.getName());
        t.setStartDate(LocalDate.now());
        t.setTotalPrice(car.getCarPrice());
        transactionService.save(t);

        ra.addFlashAttribute("successMessage",
                "Achat confirmé ! " + car.getBrand() + " " + car.getModel() + " acheté pour " + car.getCarPrice() + " $");
        return "redirect:/shop";
    }
}
