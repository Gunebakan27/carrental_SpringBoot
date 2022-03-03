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

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(USER_NOT_FOUND_MSG, userId)));

        return reservationRepository.findAllByUserId(user);

    }

    public ReservationDTO findByIdAndUserId(Long id, Long userId) throws ResourceNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(USER_NOT_FOUND_MSG, userId)));

        return reservationRepository.findByIdAndUserId(id, user)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(RESERVATION_NOT_FOUND_MSG, id)));
    }

    public List<ReservationDTO> fetchAllReservations() {
        return reservationRepository.findAllBy();
    }

    public ReservationDTO findById(Long id) {

        return reservationRepository.findByIdOrderById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(RESERVATION_NOT_FOUND_MSG, id)));
    }

    public void updateReservation(Reservation reservation, Long reservationId, Car carId) throws BadRequestException {
        boolean checkStatus = carAvailability(carId.getId(), reservation.getPickUpTime(), reservation.getDropOffTime());
        Reservation reservationExist = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(RESERVATION_NOT_FOUND_MSG, reservationId)));
        if (reservation.getPickUpTime().compareTo(reservationExist.getPickUpTime()) == 0 &&
                reservation.getDropOffTime().compareTo(reservationExist.getDropOffTime()) == 0 &&
                carId.getId().equals(reservationExist.getCar().getId())) {
            reservationExist.setStatus(reservation.getStatus());
        } else if (checkStatus)
            throw new BadRequestException("Car is laready reserved! Please choose another!");
Double totalPrice=totalPrice(reservation.getPickUpTime(), reservation.getDropOffTime(),carId.getId());
reservationExist.setTotalPrice(totalPrice);
reservationExist.setCar(carId);
reservationExist.setPickUpTime(reservation.getPickUpTime());
reservationExist.setDropOffTime(reservation.getDropOffTime());
reservationExist.setPickUpLocation(reservation.getPickUpLocation());
reservationExist.setDropOffLocation(reservation.getDropOffLocation());
reservationRepository.save(reservationExist);
    }

    public void removeById(Long id) throws BadRequestException {
        boolean reservationExists=reservationRepository.existsById(id);

        if (!reservationExists){
            throw new ResourceNotFoundException("Reservation does not exist");
        }

        reservationRepository.deleteById(id);
    }
}
