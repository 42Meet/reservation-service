package kr.meet42.reservationservice.domain.repository;

import kr.meet42.reservationservice.domain.entity.Participate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipateRepository extends JpaRepository<Participate, Long> {
}
