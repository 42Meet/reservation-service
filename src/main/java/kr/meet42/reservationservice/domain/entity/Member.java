package kr.meet42.reservationservice.domain.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Member {
    @Id
    private Long member_id;

    @Column(nullable = false)
    private String intra;

    @Builder
    public Member(Long member_id, String intra) {
        this.member_id = member_id;
        this.intra = intra;
    }
}