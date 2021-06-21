package kr.meet42.reservationservice.domain.repository;

import kr.meet42.reservationservice.domain.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findByLocation(String location);
}
