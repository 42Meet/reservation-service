package kr.meet42.reservationservice.service;

import kr.meet42.reservationservice.domain.entity.Reservation;
import kr.meet42.reservationservice.domain.repository.ReservationRepository;
import kr.meet42.reservationservice.web.dto.ReservationSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;


@RequiredArgsConstructor
@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;

    @Transactional
    public Long save(ReservationSaveRequestDto requestDto) {
        Long id = 0L;
        if (isValid(requestDto)) {
            id = reservationRepository.save(requestDto.toReservationEntity()).getId();
        }
        return id;
    }

    @Transactional
    public void delete (Long id) {
        Reservation reserve = reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 예약 내역이 없습니다. 예약id="+id));
        reservationRepository.delete(reserve);
    }


    public boolean isValid(ReservationSaveRequestDto requestDto) {
        String room_name;
        Date date;
        Time start_time;
        Time end_time;
        Reservation tmp;

        room_name = requestDto.getRoomName();
        System.out.println("room_name = " + room_name);
        date = Date.valueOf(requestDto.getDate());
        start_time = Time.valueOf(requestDto.getStart_time());
        end_time = Time.valueOf(requestDto.getEnd_time());
        // TODO: db에 room_name, date 로 reservation 리스트 가져오고 start_time, end_time 비교 ...NoSqlDB적용고려
        List<Reservation> reservations =  reservationRepository.findByRoomNameAndDate(room_name, date);
        for (Iterator<Reservation> iter = reservations.iterator(); iter.hasNext(); ) {
            tmp = iter.next();
            tmp.getStartTime();
            tmp.getEndTime();

        }
        return false;
    }
}
