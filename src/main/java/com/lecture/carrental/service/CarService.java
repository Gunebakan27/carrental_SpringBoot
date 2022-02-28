package com.lecture.carrental.service;

import com.lecture.carrental.domain.Car;
import com.lecture.carrental.domain.FileDB;
import com.lecture.carrental.dto.CarDTO;
import com.lecture.carrental.exception.ResourceNotFoundException;
import com.lecture.carrental.projection.ProjectCar;
import com.lecture.carrental.repository.CarRepository;
import com.lecture.carrental.repository.FileDBRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.ws.rs.BadRequestException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Service
public class CarService {
    private final CarRepository carRepository;
    private final FileDBRepository fileDBRepository;
    private final static String IMAGE_NOT_FOUND_MSG = "image with id %s not found";
    private final static String USER_NOT_FOUND_MSG = "User with id %s not found";

    public void add(Car car, String imageId) throws BadRequestException {
        FileDB fileDB = fileDBRepository.findById(imageId).orElseThrow(
                () -> new ResourceNotFoundException(String.format(IMAGE_NOT_FOUND_MSG, imageId)));
        Set<FileDB> fileDBS = new HashSet<>();
        fileDBS.add(fileDB);

        car.setImage(fileDBS);

        carRepository.save(car);
    }

    public CarDTO findById(Long id) throws ResourceNotFoundException {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(USER_NOT_FOUND_MSG, id)));

        return new CarDTO(car.getModel(), car.getDoors(), car.getSeats(), car.getLuggage(), car.getTransmission(),
                car.getAirConditioning(), car.getAge(), car.getPricePerHour(), car.getFuelType(), car.getBuiltIn());

    }
    public List<ProjectCar> fetchAllCars() {

        return carRepository.findAllBy();
    }
}
