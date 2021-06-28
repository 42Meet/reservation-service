package kr.meet42.reservationservice.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class ReservationDeleteRequestDto {
    private Long id;
    private String accessToken;

    @Builder
    public ReservationDeleteRequestDto(Long id, String accessToken) {
        this.id = id;
        this.accessToken = accessToken;
    }
}
