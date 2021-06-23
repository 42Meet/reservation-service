package kr.meet42.reservationservice.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReservationSaveResponseDto {
    private boolean success;

    @Builder
    public ReservationSaveResponseDto(boolean success) {
        this.success = success;
    }
}
