package kr.meet42.reservationservice.domain.entity;

import lombok.*;

import javax.persistence.*;


@Entity
@Getter @Setter
@NoArgsConstructor
public class Participate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long participate_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="reserve_id")
    private Reservation reservation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    @Builder
    public Participate(Reservation reservation, Member member) {
        this.reservation = reservation;
        this.member = member;
    }
}
