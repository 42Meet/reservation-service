package kr.meet42.reservationservice.domain.repository;

import kr.meet42.reservationservice.domain.entity.Participate;
import kr.meet42.reservationservice.domain.entity.Reservation;
import kr.meet42.reservationservice.web.dto.ReservationResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByDateAndLocationAndStatusIsNotOrderByStartTimeAsc(Date date, String location, Long status);
    List<Reservation> findByDateAndRoomNameAndStatusIsNotOrderByStartTimeAsc(Date date, String room_name, Long status);
    List<Reservation> findByDateAndStatusIsNotOrderByStartTimeAsc(Date date, Long status);
    List<Reservation> findByRoomNameAndDateAndStatusIsNot(String roomName, Date date, Long status);
    Optional<Reservation> findByIdAndStatus(Long id, Long status);

    List<Reservation> findAllByStatusOrderByDateAsc(Long status);

    List<Reservation> findByStatus(Long status);

    List<Reservation> findByStatusIsNot(Long Status);

    Page<Reservation> findByStatus(Long status, Pageable pageable);

    Page<Reservation> findByStatusOrderByDateAscStartTimeAsc(Long status, Pageable pageable);

    Page<Reservation> findByStatusOrderByDateDescStartTimeDesc(Long status, Pageable pageable);

    int countByStatus(Long status);

    long countByLeaderNameAndDateBetweenAndStatusIsNot(String leaderName, Date start, Date end, Long status);
}
