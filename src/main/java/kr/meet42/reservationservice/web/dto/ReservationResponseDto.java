package kr.meet42.reservationservice.web.dto;

import jdk.jfr.Name;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Date;
import java.sql.Time;
import java.util.List;

@Getter
@NoArgsConstructor
public class ReservationResponseDto {
    private Long id;
    private String leaderName;
    private String roomName;
    private String location;
    private Date date;
    private Time startTime;
    private Time endTime;
    private String department;
    private String purpose;
    private String title;
    private Long status;
    private String content;
    private List<String> members;

    @Builder
    public ReservationResponseDto(Long id, String leaderName, String roomName, String location, Date date, Time startTime, Time endTime, String department, String purpose, String title, Long status, String content, List<String> members) {
        this.id = id;
        this.leaderName = leaderName;
        this.roomName = roomName;
        this.location = location;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.department = department;
        this.purpose = purpose;
        this.title = title;
        this.status = status;
        this.content = content;
        this.members = members;
    }
}
