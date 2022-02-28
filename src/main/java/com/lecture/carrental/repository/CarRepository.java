package com.lecture.carrental.repository;

import com.lecture.carrental.domain.Car;
import com.lecture.carrental.projection.ProjectCar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository
public interface CarRepository extends JpaRepository<Car,Long> {

    List<ProjectCar> findAllBy();
}
