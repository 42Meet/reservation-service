package kr.meet42.reservationservice.web.dto;

import lombok.Getter;

import java.util.List;
import java.util.Optional;

@Getter
public class AdminDecideRequestDto {
    private Optional<Long> id;
    private Optional<Boolean> result;
    private String accessToken;

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
