package kr.meet42.reservationservice.domain.entity;

import kr.meet42.reservationservice.domain.repository.ReservationRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.sql.Date;
import java.sql.Time;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
public class ReservationRepositoryTest {
    @Autowired
    ReservationRepository reservationRepository;
    @AfterEach
    public void cleanup() {
        reservationRepository.deleteAll();
    }

//    @Test
//    public void 예약저장_불러오기() {
//        String leaderName = "sebaek";
//        String location = "개포";
//        Date date = Date.valueOf("2021-06-18");
//        Time startTime = Time.valueOf("01:00:00");
//        Time endTime = Time.valueOf("03:00:00");
//        String roomName = "창경궁";
//
//        reservationRepository.save(Reservation.builder()
//                .leaderName(leaderName)
//                .roomName(roomName)
//                .location(location)
//                .date(date)
//                .startTime(startTime)
//                .endTime(endTime)
//                .build());
//
//        List<Reservation> ReservationList = reservationRepository.findAll();
//
//        Reservation reserve = ReservationList.get(0);
//        assertThat(reserve.getLeaderName()).isEqualTo("sebaek");
//        assertThat(reserve.getLocation()).isEqualTo("개포");
//    }

}
