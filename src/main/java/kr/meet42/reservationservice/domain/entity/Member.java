package kr.meet42.reservationservice.domain.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long member_id;

    @Column(nullable = false)
    private String intra;

    @Builder
    public Member(String intra) {
        this.intra = intra;
    }
}