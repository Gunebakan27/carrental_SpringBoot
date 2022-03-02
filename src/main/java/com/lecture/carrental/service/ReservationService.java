package com.lecture.carrental.service;

import com.lecture.carrental.domain.Car;
import com.lecture.carrental.domain.Reservation;
import com.lecture.carrental.domain.User;
import com.lecture.carrental.domain.enumeration.ReservationStatus;
import com.lecture.carrental.dto.ReservationDTO;
import com.lecture.carrental.exception.ResourceNotFoundException;
import com.lecture.carrental.repository.CarRepository;
import com.lecture.carrental.repository.ReservationRepository;
import com.lecture.carrental.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.ws.rs.BadRequestException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final CarRepository carRepository;
    private final static String USER_NOT_FOUND_MSG = "User with id %d not found!";
    private final static String CAR_NOT_FOUND_MSG = "Car with id %d not found!";
    private final static String RESERVATION_NOT_FOUND_MSG = "Reservation with id %d not found!";

    public void addReservation(Reservation reservation, Long userId, Car carId) throws BadRequestException {
        boolean checkStatus = carAvailability(carId.getId(), reservation.getPickUpTime(), reservation.getDropOffTime());

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(USER_NOT_FOUND_MSG, userId)));
        if (!checkStatus)
            reservation.setStatus(ReservationStatus.CREATED);
        else
            throw new BadRequestException("Car is already reserved! Please select another time.");
        reservation.setCar(carId);
        reservation.setUser(user);

        Double totalPrice = totalPrice(reservation.getPickUpTime(), reservation.getDropOffTime(), carId.getId());

        reservation.setTotalPrice(totalPrice);
        reservationRepository.save(reservation);

    }

    public Double totalPrice(LocalDateTime pickUpTime, LocalDateTime dropOffTime, Long carId) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(CAR_NOT_FOUND_MSG, carId)));

        Long hours = (new Reservation()).getTotalHours(pickUpTime, dropOffTime);

        return car.getPricePerHour() * hours;
    }

    public boolean carAvailability(Long id, LocalDateTime pickUpTime, LocalDateTime dropOffTime) {
        List<Reservation> checkStatus = reservationRepository.checkStatus(id, pickUpTime, dropOffTime,
                ReservationStatus.DONE, ReservationStatus.CANCELED);
        return checkStatus.size() > 0;
    }

    public List<ReservationDTO> findAllByUserId(Long userId) {

        User user=userRepository.findById(userId)
                .orElseThrow(()->new ResourceNotFoundException(String.format(USER_NOT_FOUND_MSG,userId)));

        return reservationRepository.findAllByUserId(user);

    }

    public ReservationDTO findByIdAndUserId(Long id, Long userId) {
        Optional<User> user=userRepository.findById(userId);

        return reservationRepository.findByIdAndUserId(id,user.get())
                .orElseThrow(()->new ResourceNotFoundException(String.format(RESERVATION_NOT_FOUND_MSG,id)));
    }

    public List<ReservationDTO> fetchAllReservations() {
        return reservationRepository.findAllBy();
    }

    public ReservationDTO findById(Long id) {

        return reservationRepository.findByIdOrderById(id)
                .orElseThrow(()->new ResourceNotFoundException(String.format(RESERVATION_NOT_FOUND_MSG,id)));
    }
}
