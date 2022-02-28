package com.lecture.carrental.dto;

import com.lecture.carrental.domain.FileDB;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CarDTO {

    @Size(max = 30, message = "Size is exceeded")
    @NotNull(message = "Please enter the car model")
    private String model;

    @NotNull(message = "Please enter the car doors")
    private Integer doors;

    @NotNull(message = "Please enter the seat numbers")
    private Integer seats;

    @NotNull(message = "Please enter the car luggage")
    private Integer luggage;

    @Size(max = 30)
    @NotNull(message = "Please enter the car transmission")
    private String transmission;

    @NotNull(message = "Please enter the air condition")
    private Boolean airConditioning;

    @NotNull(message = "Please enter the car age")
    private Integer age;

    @NotNull(message = "Please enter the price per hour")
    private Double pricePerHour;

    @Size(max = 30)
    @NotNull(message = "Please enter the car fuel type")
    private String fuelType;

    private Boolean builtIn;

    private Set<FileDB> image;

    public CarDTO(String model, Integer doors, Integer seats, Integer luggage, String transmission, Boolean airConditioning, Integer age, Double pricePerHour, String fuelType, Boolean builtIn) {
        this.model = model;
        this.doors = doors;
        this.seats = seats;
        this.luggage = luggage;
        this.transmission = transmission;
        this.airConditioning = airConditioning;
        this.age = age;
        this.pricePerHour = pricePerHour;
        this.fuelType = fuelType;
        this.builtIn = builtIn;
    }
}