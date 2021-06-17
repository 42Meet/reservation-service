package kr.meet42.reservationservice.domain.repository;

import kr.meet42.reservationservice.domain.entity.MeetingRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetingRoomRepository extends JpaRepository<MeetingRoom, Long> {
}
