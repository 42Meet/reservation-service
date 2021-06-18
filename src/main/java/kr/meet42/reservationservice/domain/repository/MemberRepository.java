package kr.meet42.reservationservice.domain.repository;

import kr.meet42.reservationservice.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
