package kr.meet42.reservationservice.domain.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String roomName;

    @Column(nullable = false)
    private String location;

    @Builder
    public Room(String location, String roomName) {
        this.location = location;
        this.roomName = roomName;
    }
}
