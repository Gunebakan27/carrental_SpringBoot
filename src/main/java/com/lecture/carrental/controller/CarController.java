package com.lecture.carrental.controller;

import com.lecture.carrental.domain.Car;
import com.lecture.carrental.dto.CarDTO;
import com.lecture.carrental.projection.ProjectCar;
import com.lecture.carrental.service.CarService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@AllArgsConstructor
@RestController
@Produces(MediaType.APPLICATION_JSON)
@RequestMapping("/car")
public class CarController {

    public CarService carService;

    @PostMapping("/admin/{imageId}/add")
    public ResponseEntity<Map<String, Boolean>> addCar(@PathVariable String imageId,
                                                       @Valid @RequestBody Car car) {
        carService.add(car, imageId);
        Map<String, Boolean> map = new HashMap<>();
        map.put("Car added successfully", true);

        return new ResponseEntity<>(map, HttpStatus.CREATED);

    }
    @GetMapping("")
    @PreAuthorize("hasRole('CUSTOMER')or hasRole('ADMIN')")
    public ResponseEntity<CarDTO>getCarById(HttpServletRequest request){
        Long id= (Long) request.getAttribute("id");
        CarDTO carDTO=carService.findById(id);
        return new ResponseEntity<>(carDTO,HttpStatus.OK);
    }
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ProjectCar>> getAllCars() {
        List<ProjectCar> cars = carService.fetchAllCars();
        return new ResponseEntity<>(cars, HttpStatus.OK);
    }
}
