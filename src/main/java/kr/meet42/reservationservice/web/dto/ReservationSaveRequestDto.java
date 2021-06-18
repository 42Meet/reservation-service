package kr.meet42.reservationservice.web.dto;

import kr.meet42.reservationservice.domain.entity.Member;
import kr.meet42.reservationservice.domain.entity.Participate;
import kr.meet42.reservationservice.domain.entity.Reservation;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.stylesheets.LinkStyle;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class ReservationSaveRequestDto {
    private String location;
    private String roomName;
    private String date;
    private String startTime;
    private String endTime;
    private String leaderName;
    private ArrayList<String> members; // 배열?



    //
//    location: "개포",
//    room_name: "창경궁",
//    date: "2021-06-18",
//    start_time: "18:00",
//    end_time: "20:00",
//    leader_name: "taehk]im"
//    member: {
//        1: "sebaek",
//                2: "jakang",
//                3: "esim"
    @Builder
    public ReservationSaveRequestDto(String location, String roomName, String date, String startTime, String endTime, String leaderName, ArrayList<String> members) {
        this.location = location;
        this.roomName = roomName;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.leaderName = leaderName;
        this.members = members;
    }

//    public Member toMemberEntity() {
//
//    }
//
//    public Participate toParticipateEntity() {
//
//    }

    public Reservation toReservationEntity() {
        return Reservation.builder()
                .leaderName(leaderName)
                .location(location)
                .roomName(roomName)
                .date(Date.valueOf(date))
                .startTime(Time.valueOf(startTime))
                .endTime(Time.valueOf(endTime))
                .build();
    }
}
