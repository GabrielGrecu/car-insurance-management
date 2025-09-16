package com.example.carins.web.dto;

import com.example.carins.model.Car;
import com.example.carins.model.Owner;

public record CarDto(Long id, String vin, String make, String model, int year, Long ownerId, String ownerName, String ownerEmail) {

    public static CarDto fromEntity(Car car) {
        if (car == null) {
            return null;
        }

        Owner owner = car.getOwner();
        return new CarDto(
                car.getId(),
                car.getVin(),
                car.getMake(),
                car.getModel(),
                car.getYearOfManufacture(),
                owner != null ? owner.getId() : null,
                owner != null ? owner.getName() : null,
                owner != null ? owner.getEmail() : null
        );
    }
}