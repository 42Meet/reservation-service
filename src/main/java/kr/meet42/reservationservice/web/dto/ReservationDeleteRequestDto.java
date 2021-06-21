package kr.meet42.reservationservice.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class ReservationDeleteRequestDto {
    private Long id;
    private String jwt;

    @Builder
    public ReservationDeleteRequestDto(Long id, String jwt) {
        this.id = id;
        this.jwt = jwt;
    }
}
