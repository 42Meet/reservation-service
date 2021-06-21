package kr.meet42.reservationservice.domain.repository;

import kr.meet42.reservationservice.domain.entity.Participate;
import kr.meet42.reservationservice.domain.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParticipateRepository extends JpaRepository<Participate, Long> {
    List<Participate> findByReservation(Reservation reservation);
}