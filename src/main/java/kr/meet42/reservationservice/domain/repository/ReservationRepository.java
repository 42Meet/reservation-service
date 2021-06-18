package kr.meet42.reservationservice.domain.repository;

import kr.meet42.reservationservice.domain.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByDateAndLocationOrderByStartTimeAsc(Date date, String location);
    List<Reservation> findByDateAndRoomNameOrderByStartTimeAsc(Date date, String room_name);
    List<Reservation> findByDateOrderByStartTimeAsc(Date date);
}
