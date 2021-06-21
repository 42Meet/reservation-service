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
import java.util.Iterator;
import java.util.List;

@Getter
@NoArgsConstructor
public class ReservationSaveRequestDto {
    private String location;
    private String roomName;
    private String date;
    private String startTime;
    private String endTime;
    private String leader_id;
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
        this.startTime = start_time;
        this.endTime = end_time;
        this.leader_id = leader_name;
        this.members = members;
    }


    public Participate toParticipateEntity(Reservation reservation, Member member){

        return Participate.builder()
                .reservation(reservation)
                .member(member)
                .build();
    }

//    public ArrayList<Long> toMemberEntities() {
//        String tmp_intra;
//        ArrayList<Long> member_ids = new ArrayList<Long>();
//        for (Iterator<String> iter = members.iterator(); iter.hasNext(); ) {
//            tmp_intra = iter.next();
//            member_ids.add(toMemberEntity(tmp_intra).getMember_id());
//        }
//        return member_ids;
//    }

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
                .startTime(Time.valueOf(startTime))
                .endTime(Time.valueOf(endTime))
                .build();
    }
}
