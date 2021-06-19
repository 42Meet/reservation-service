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
    private String start_time;
    private String end_time;
    private String leader_name;
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
    public ReservationSaveRequestDto(String location, String roomName, String date, String start_time, String end_time, String leader_name, ArrayList<String> members){
        this.location = location;
        this.roomName = roomName;
        this.date = date;
        this.start_time = start_time;
        this.end_time = end_time;
        this.leader_name = leader_name;
        this.members = members;
    }


    public Participate toParticipateEntity(Reservation reservation, Member member){
        return Participate.builder()
                .reservation(reservation)
                .member(member)
                .build();
    }

    public Member toMemberEntity(String intra) {
        return Member.builder()
                .intra(intra)
                .build();
    }

    public Reservation toReservationEntity() {
        return Reservation.builder()
                .leader_id(1000L)
                .location(location)
                .roomName(roomName)
                .date(Date.valueOf(date))
                .startTime(Time.valueOf(start_time))
                .endTime(Time.valueOf(end_time))
                .build();
    }
}
