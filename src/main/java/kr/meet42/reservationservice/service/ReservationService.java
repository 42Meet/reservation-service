package kr.meet42.reservationservice.service;

import kr.meet42.reservationservice.domain.entity.Reservation;
import kr.meet42.reservationservice.domain.repository.ReservationRepository;
import kr.meet42.reservationservice.web.dto.ReservationSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import static org.graalvm.compiler.nodeinfo.Verbosity.Id;

@RequiredArgsConstructor
@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;

    @Transactional
    public Long save(ReservationSaveRequestDto requestDto) {
        return reservationRepository.save(requestDto.toEntity()).getId();
    }

    @Transactional
    public void delete (Long id) {
        Reservation reserve = reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 예약 내역이 없습니다. 예약id="+id));
        reservationRepository.delete(reserve);
    }


}
