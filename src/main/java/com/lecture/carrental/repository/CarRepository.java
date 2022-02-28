package com.lecture.carrental.repository;

import com.lecture.carrental.domain.Car;
import com.lecture.carrental.dto.CarDTO;
import com.lecture.carrental.exception.ResourceNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
@Repository
public interface CarRepository extends JpaRepository<Car,Long> {

    @Query("Select new com.lecture.carrental.dto.CarDTO(c) from Car c")
    List<CarDTO> findAllCar();

//    @Query("Select new com.lecture.carrental.dto.CarDTO(c) from Car c where car.id=?1")
   Optional<CarDTO> findByIdOrderById(Long id) throws ResourceNotFoundException;
}
