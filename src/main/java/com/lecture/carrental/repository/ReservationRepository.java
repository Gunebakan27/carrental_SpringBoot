package com.lecture.carrental.repository;

import com.lecture.carrental.domain.Reservation;
import com.lecture.carrental.domain.User;
import com.lecture.carrental.domain.enumeration.ReservationStatus;
import com.lecture.carrental.dto.ReservationDTO;
import com.lecture.carrental.exception.ResourceNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    //@Query("select  new com.lecture.carrental.dto.ReservationDTO(r) from Reservation r")
    List<ReservationDTO> findAllBy();

    Optional<ReservationDTO>findByIdOrderById(Long id) throws ResourceNotFoundException;

    //    @Query("select new com.lecture.carrental.dto.ReservationDTO(r) from Reservation r where r.user.id=?1")
    List<ReservationDTO> findAllByUserId(User user);


    //@Query("select  new com.lecture.carrental.dto.ReservationDTO(r) from Reservation r where r.id=?1 and r.user=?2")
    Optional<ReservationDTO> findByIdAndUserId(Long id, User user) throws ResourceNotFoundException;

    @Query("Select r from Reservation r " +
            "Left Join fetch r.car c " +
            "Left Join fetch c.image img " +
            "Left Join fetch r.user u " +
            "where (c.id=?1 and r.status <> ?4 " +
            "and r.status<>?5 and ?2 between r.pickUpTime " +
            "and r.dropOffTime) or (c.id=?1 and r.status<>?4 and " +
            "r.status<>?5 and ?3 between r.pickUpTime and r.dropOffTime)")
    List<Reservation> checkStatus(Long id, LocalDateTime pickUpTime, LocalDateTime dropOffTime,
                                  ReservationStatus done, ReservationStatus canceled);

}
