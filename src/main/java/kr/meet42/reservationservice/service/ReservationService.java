package kr.meet42.reservationservice.service;

import kr.meet42.reservationservice.domain.entity.Reservation;
import kr.meet42.reservationservice.domain.repository.ReservationRepository;
import kr.meet42.reservationservice.web.dto.ReservationSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;


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


}
