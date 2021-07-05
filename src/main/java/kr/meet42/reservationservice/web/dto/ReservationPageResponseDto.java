package kr.meet42.reservationservice.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ReservationPageResponseDto {

    @JsonProperty("reservations")
    private List<ReservationResponseDto> reservationResponseDtos;
    private int currentPage;
    private int maxPage;

    @Builder
    public ReservationPageResponseDto(List<ReservationResponseDto> reservationResponseDtos, int currentPage, int maxPage){
        this.reservationResponseDtos = reservationResponseDtos;
        this.currentPage = currentPage;
        this.maxPage = maxPage;
    }

}
