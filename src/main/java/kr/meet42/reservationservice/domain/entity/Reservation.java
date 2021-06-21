package kr.meet42.reservationservice.domain.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Array;
import java.sql.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "leader_name", nullable = false)
    private String leaderName;

    @Column(name = "room_name", nullable = false) // name을 지정해서 findby~에 매핑될수있도록함
    private String roomName;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private Date date;

    @Column(name = "start_time", nullable = false)
    private Time startTime;

    @Column(name = "end_time", nullable = false)
    private Time endTime;

    @Column(name = "department", nullable = false)
    private String department;

    @Column(name = "purpose", nullable = false)
    private String purpose;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "status", nullable = false)
    private Long status;

    @Column(name = "content", nullable = false)
    private String content;

    @OneToMany(fetch = FetchType.EAGER)
    private List<Participate> participate;

    @Builder
    public Reservation(String leaderName, String roomName, String location, Date date, Time startTime, Time endTime, String department, String purpose, String title, Long status, String content, List<Participate> participate) {
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
        this.participate = participate;
    }
}
