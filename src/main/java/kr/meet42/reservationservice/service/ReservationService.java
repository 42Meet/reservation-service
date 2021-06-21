package kr.meet42.reservationservice.service;

import kr.meet42.reservationservice.domain.entity.Member;
import kr.meet42.reservationservice.domain.entity.Reservation;
import kr.meet42.reservationservice.domain.repository.MemberRepository;
import kr.meet42.reservationservice.domain.repository.ParticipateRepository;
import kr.meet42.reservationservice.domain.repository.ReservationRepository;
import kr.meet42.reservationservice.utils.JWTUtil;
import kr.meet42.reservationservice.web.dto.ReservationSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.sql.Time;
import java.util.Iterator;


@RequiredArgsConstructor
@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final MemberRepository memberRepository;
    private final ParticipateRepository participateRepository;
    private final JWTUtil jwtUtil;

    @Transactional
    public boolean save(ReservationSaveRequestDto requestDto) {
        Reservation reservation;

        if (isValid(requestDto)) {
            reservation = reservationRepository.save(requestDto.toReservationEntity());
            memberRepository.save(requestDto.toMemberEntity(requestDto.getLeaderName()));
            for (Iterator<String> iter = requestDto.getMembers().iterator(); iter.hasNext(); ) {
                Member member = requestDto.toMemberEntity(iter.next());
                memberRepository.save(member);
                participateRepository.save(requestDto.toParticipateEntity(reservation, member));
            }
            return true; // 저장 성공
        }
        return false; // 저장 실패
    }

    @Transactional // Question: 여기서 Transactional이 필요한가? 그냥 조횐데?
    public List<Reservation> findMyReservation(HttpServletRequest request) {

        List<Reservation> reservations = new ArrayList<Reservation>();
        // TODO: 토큰으로부터 intraName받아오기
//        String jwt = request.getHeader("access-token");
//        String intra = jwtUtil.validateAndExtract(jwt);
//        System.out.println("intra = " + intra);
        String intra = "sebaek";
        List<Member> members = memberRepository.findByIntra(intra);
        for (Iterator<Member> iter = members.iterator(); iter.hasNext(); ) {
            Member member = iter.next();
            reservations.add(participateRepository.findByMember(member).getReservation());
        }
        return reservations;
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
            return reservationRepository.findByDateAndRoomNameOrderByStartTimeAsc(Date.valueOf(paramMap.get("date")), paramMap.get("roomName"));
        }
        else if (paramMap.containsKey("date") && paramMap.containsKey("location")) {
            return reservationRepository.findByDateAndLocationOrderByStartTimeAsc(Date.valueOf(paramMap.get("date")), paramMap.get("location"));
        }
        else if (paramMap.containsKey("date")) {
            return reservationRepository.findByDateOrderByStartTimeAsc(Date.valueOf(paramMap.get("date")));
        }
        return new ArrayList<Reservation>();
    }

    public boolean isValid(ReservationSaveRequestDto requestDto) {
        String room_name;
        Date date;
        Time start_time;
        Time end_time;
        Reservation tmp;
        Time tmp_start;
        Time tmp_end;

        room_name = requestDto.getRoomName();
        System.out.println("room_name = " + room_name);
        date = Date.valueOf(requestDto.getDate());
        start_time = Time.valueOf(requestDto.getStartTime());
        end_time = Time.valueOf(requestDto.getEndTime());
        // TODO: db에 room_name, date 로 reservation 리스트 가져오고 start_time, end_time 비교 ...NoSqlDB적용고려
        List<Reservation> reservations =  reservationRepository.findByRoomNameAndDate(room_name, date);
        for (Iterator<Reservation> iter = reservations.iterator(); iter.hasNext(); ) {
            tmp = iter.next();
            tmp_start = tmp.getStartTime();
            tmp_end = tmp.getEndTime();
            if (end_time.compareTo(tmp_start) > 0 && end_time.compareTo(tmp_end) <= 0)
                return false;
            else if (start_time.compareTo(tmp_end) < 0 && start_time.compareTo(tmp_start) >= 0)
                return false;
            else if (start_time.compareTo(tmp_start) >= 0 && end_time.compareTo(tmp_end) <= 0)
                return false;
        }
        return true;
    }
}
