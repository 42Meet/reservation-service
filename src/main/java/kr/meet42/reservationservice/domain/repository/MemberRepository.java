package kr.meet42.reservationservice.domain.repository;

import kr.meet42.reservationservice.domain.entity.Member;
import kr.meet42.reservationservice.domain.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Date;
import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findByIntra(String intra);
}
