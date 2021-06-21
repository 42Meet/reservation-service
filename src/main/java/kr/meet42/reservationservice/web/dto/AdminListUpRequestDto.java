package kr.meet42.reservationservice.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class AdminListUpRequestDto {
    private String location;
    private List<String> roomName;

    @Builder
    public AdminListUpRequestDto(String location, List<String> roomName) {
        this.location = location;
        this.roomName = roomName;
    }

}
