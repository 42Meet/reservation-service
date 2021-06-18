package kr.meet42.reservationservice.domain.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long leader_id;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private Date date;

    @Column(nullable = false)
    private Time startTime;

    @Column(nullable = false)
    private Time endTime;

    @Builder
    public Reservation(Long leader_id, String location, Date date, Time startTime, Time endTime) {
        this.leader_id = leader_id;
        this.location = location;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
