package com.example.carins.service;

import com.example.carins.model.Car;
import com.example.carins.repo.CarRepository;
import com.example.carins.repo.InsurancePolicyRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

@Service
public class CarService {

    private final CarRepository carRepository;
    private final InsurancePolicyRepository policyRepository;

    public CarService(CarRepository carRepository, InsurancePolicyRepository policyRepository) {
        this.carRepository = carRepository;
        this.policyRepository = policyRepository;
    }

    public List<Car> listCars() {
        return carRepository.findAll();
    }

    public boolean isInsuranceValid(Long carId, LocalDate date) {
        if (carId == null || date == null) {
            return false;
        }

        if (!carRepository.existsById(carId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Car with ID " + carId + " not found");
        }

        return policyRepository.existsActiveOnDate(carId, date);
    }

    public LocalDate validateAndParseDate(String dateString) {
        if (dateString == null || dateString.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Date parameter is required");
        }

        try {
            LocalDate date = LocalDate.parse(dateString);

            validateDateRange(date);

            return date;
        } catch (DateTimeParseException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Invalid date format. Please use ISO format YYYY-MM-DD");
        }
    }

    private void validateDateRange(LocalDate date) {
        LocalDate currentDate = LocalDate.now();
        LocalDate minDate = LocalDate.of(2000, 1, 1);
        LocalDate maxDate = currentDate.plusYears(10);

        if (date.isBefore(minDate)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Date cannot be before " + minDate);
        }

        if (date.isAfter(maxDate)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Date cannot be more than 10 years in the future");
        }
    }
}