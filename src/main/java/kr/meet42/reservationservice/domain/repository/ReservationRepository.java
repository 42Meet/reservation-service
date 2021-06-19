package kr.meet42.reservationservice.domain.repository;

import kr.meet42.reservationservice.domain.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import java.sql.Date;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByDateAndLocationOrderByStartTimeAsc(Date date, String location);
    List<Reservation> findByDateAndRoomName(Date date, String room_name);
    List<Reservation> findByDate(Date date);
    List<Reservation> findByRoomNameAndDate(String roomName, Date date);
}