package kr.meet42.reservationservice.domain.repository;

import kr.meet42.reservationservice.domain.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
}
