package kr.meet42.reservationservice.domain.entity;

import kr.meet42.reservationservice.domain.repository.MeetingRoomRepository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class MeetingRoomTest {
    @Autowired
    MeetingRoomRepository meetingRoomRepository;

    @AfterEach
    public void cleanup() {
        meetingRoomRepository.deleteAll();
    }

    @Test
    public void 미팅룸저장_불러오기() {
        // given
        String location = "개포";
        boolean availability = true;
        Date date = Date.valueOf("2021-06-18");
        Time time = Time.valueOf("00:00:00");
        Long room_type = 1L;

        meetingRoomRepository.save(MeetingRoom.builder()
                .location(location)
                .availability(availability)
                .date(date)
                .time(time)
                .room_type(room_type)
                .build());

        List<MeetingRoom> MeetingRoomList = meetingRoomRepository.findAll();

        MeetingRoom room = MeetingRoomList.get(0);
        assertThat(room.getRoom_type()).isEqualTo(1L);
        assertThat(room.getLocation()).isEqualTo("개포");
    }
}
