package kr.meet42.reservationservice.service;

import kr.meet42.reservationservice.domain.entity.Reservation;
import kr.meet42.reservationservice.domain.repository.ReservationRepository;
import kr.meet42.reservationservice.web.dto.ReservationSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@RequiredArgsConstructor
@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;

    @Transactional
    public Long save(ReservationSaveRequestDto requestDto) {
//        System.out.println("requestDto = " + requestDto.getDate().toString());
        return reservationRepository.save(requestDto.toReservationEntity()).getId();
    }

    @Transactional
    public void delete (Long id) {
        Reservation reserve = reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 예약 내역이 없습니다. 예약id="+id));
        reservationRepository.delete(reserve);
    }

    @Transactional
    public List<Reservation> findAllReservationByParam(Map<String, String> paramMap) {
        if (paramMap.containsKey("date") && paramMap.containsKey("roomName")) {
            return reservationRepository.findByDateAndRoomName(Date.valueOf(paramMap.get("date")), paramMap.get("roomName"));
        }
        else if (paramMap.containsKey("date") && paramMap.containsKey("location")) {
            return reservationRepository.findByDateAndLocationOrderByStartTimeAsc(Date.valueOf(paramMap.get("date")), paramMap.get("location"));
        }
        else if (paramMap.containsKey("date")) {
            return reservationRepository.findByDate(Date.valueOf(paramMap.get("date")));
        }
        return new ArrayList<Reservation>();
    }
}
