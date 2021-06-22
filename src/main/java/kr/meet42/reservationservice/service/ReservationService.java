package kr.meet42.reservationservice.service;

import com.sun.xml.bind.v2.TODO;
import kr.meet42.reservationservice.domain.entity.Member;
import kr.meet42.reservationservice.domain.entity.Participate;
import kr.meet42.reservationservice.domain.entity.Reservation;
import kr.meet42.reservationservice.domain.repository.MemberRepository;
import kr.meet42.reservationservice.domain.repository.ParticipateRepository;
import kr.meet42.reservationservice.domain.repository.ReservationRepository;
import kr.meet42.reservationservice.utils.JWTUtil;
import kr.meet42.reservationservice.web.dto.ReservationResponseDto;
import kr.meet42.reservationservice.web.dto.ReservationSaveRequestDto;
import kr.meet42.reservationservice.web.dto.ReservationDeleteRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.sql.Date;
import java.util.*;
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
    public ResponseEntity<?> save(ReservationSaveRequestDto requestDto) {
        Reservation reservation;

        if (isValid(requestDto)) {
            requestDto.setStatus(1L);
            reservation = reservationRepository.save(requestDto.toReservationEntity());
            requestDto.getMembers().add(requestDto.getLeaderName());
            for (Iterator<String> iter = requestDto.getMembers().iterator(); iter.hasNext(); ) {
                Member member = requestDto.toMemberEntity(iter.next());
                memberRepository.save(member);
                participateRepository.save(requestDto.toParticipateEntity(reservation, member));
            }
            return new ResponseEntity<>(HttpStatus.OK); // 저장 성공
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 저장 실패
    }

    @Transactional // Question: 여기서 Transactional이 필요한가? 그냥 조횐데?
    public List<List<ReservationResponseDto>> findMyReservation(HttpServletRequest request) {
        List<List<ReservationResponseDto>> res = new ArrayList<>(new ArrayList<>());
        List<ReservationResponseDto> proc = new ArrayList<>();
        List<ReservationResponseDto> end = new ArrayList<>();
        List<ReservationResponseDto> sche = new ArrayList<>();
//        String intra = "sebaek";
        String intra = jwtUtil.validateAndExtract(request.getHeader("access_token"));
        List<Member> members = memberRepository.findByIntra(intra);
        for (Iterator<Member> iter = members.iterator(); iter.hasNext();) {
            Member member = iter.next();
            Participate participate = participateRepository.findByMember(member);
            setReservationStatus(participate);
            Optional<Reservation> procReservation = reservationRepository.findByIdAndStatus(participate.getReservation().getId(), 1L);
            if (procReservation.isPresent())
                proc.add(procReservation.get().toResponseDto(getMembers(procReservation.get())));
            Optional<Reservation> endReservation = reservationRepository.findByIdAndStatus(participate.getReservation().getId(), 0L);
            if (endReservation.isPresent())
                end.add(endReservation.get().toResponseDto(getMembers(endReservation.get())));
            Optional<Reservation> scheReservation = reservationRepository.findByIdAndStatus(participate.getReservation().getId(), 2L);
            if (scheReservation.isPresent())
                sche.add(scheReservation.get().toResponseDto(getMembers(scheReservation.get())));
        }
        listAscSort(proc);
        listAscSort(sche);
        listDescSort(end);
        res.add(proc);
        res.add(sche);
        res.add(end);
        return res;
    }

    @org.springframework.transaction.annotation.Transactional
    public void setReservationStatus(Participate participate) {
        Reservation target = participate.getReservation();
        if (target.getDate().compareTo(Calendar.getInstance().getTime()) == 0) {
            // 시작시간이 현재시간보다 크다면
            if (target.getStartTime().compareTo(Calendar.getInstance().getTime()) > 0) {
                target.setStatus(2L);
            }
            // 사이에 있으면
            else if (target.getStartTime().compareTo(Calendar.getInstance().getTime()) < 0
                    && target.getEndTime().compareTo(Calendar.getInstance().getTime()) > 0)
                target.setStatus(1L);
            else
                target.setStatus(0L);
        }
        // 더 늦으면 즉, 예정되어있으면
        else if (target.getDate().compareTo(Calendar.getInstance().getTime()) > 0)
            target.setStatus(2L);
        else
            target.setStatus(0L);
    }

    private void listAscSort(List<ReservationResponseDto> list) {
        Collections.sort(list, new Comparator<ReservationResponseDto>() {
            @Override
            public int compare(ReservationResponseDto b1, ReservationResponseDto b2) {
                if (b1.getDate().compareTo(b2.getDate()) == 0) {
                    return b1.getStartTime().compareTo(b2.getStartTime());
                } else
                    return b1.getDate().compareTo(b2.getDate());
            }
        });
    }

    private void listDescSort(List<ReservationResponseDto> list) {
        Collections.sort(list, new Comparator<ReservationResponseDto>() {
            @Override
            public int compare(ReservationResponseDto b1, ReservationResponseDto b2) {
                if (b1.getDate().compareTo(b2.getDate()) == 0) {
                    return b2.getStartTime().compareTo(b1.getStartTime());
                } else
                    return b2.getDate().compareTo(b1.getDate());
            }
        });
    }

    @Transactional
    public ResponseEntity<?> delete (ReservationDeleteRequestDto dto) {
//        Reservation reserve = reservationRepository.findById(dto.getId())
//                .orElseThrow(() -> new IllegalArgumentException("해당 예약 내역이 없습니다. 예약id="+ dto.getId()));
        Optional<Reservation> finded = reservationRepository.findById(dto.getId());
        if (finded.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else {
            // TODO : JWT 연결 되면 주석 풀어서 leader만 삭제할 수 있도록 수정
//            if (dto.getJwt() != finded.get().getLeaderName())
//                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            List<Participate> parti = participateRepository.findByReservation(finded.get());
            for (Iterator<Participate> iter = parti.iterator(); iter.hasNext();) {
                participateRepository.delete(iter.next());
            }
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @Transactional
    public List<ReservationResponseDto> findAllReservationByParam(Map<String, String> paramMap) {
        List<Reservation> res = new ArrayList<>();
        if (paramMap.containsKey("date") && paramMap.containsKey("roomName")) {
            res = reservationRepository.findByDateAndRoomNameOrderByStartTimeAsc(Date.valueOf(paramMap.get("date")), paramMap.get("roomName"));
        }
        else if (paramMap.containsKey("date") && paramMap.containsKey("location")) {
            res = reservationRepository.findByDateAndLocationOrderByStartTimeAsc(Date.valueOf(paramMap.get("date")), paramMap.get("location"));
        }
        else if (paramMap.containsKey("date")) {
            res = reservationRepository.findByDateOrderByStartTimeAsc(Date.valueOf(paramMap.get("date")));
        }
        if (!res.isEmpty()) {
            return getReservationResponseDtos(res);
        }
        return new ArrayList<ReservationResponseDto>();
    }

    private List<ReservationResponseDto> getReservationResponseDtos(List<Reservation> res) {
        List<ReservationResponseDto> dtos = new ArrayList<>();
        for (Iterator<Reservation> iter = res.iterator(); iter.hasNext();) {
            Reservation cur = iter.next();
            dtos.add(cur.toResponseDto(getMembers(cur)));
        }
        return dtos;
    }

    public ArrayList<String> getMembers(Reservation reservation) {
        ArrayList<String> ret = new ArrayList<>();
        List<Participate> participate = participateRepository.findByReservation(reservation);
        for (Iterator<Participate> iter = participate.iterator(); iter.hasNext();) {
            Optional<Member> member = memberRepository.findById(iter.next().getMember().getMember_id());
            if (member.isPresent())
                ret.add(member.get().getIntra());
        }
        return ret;
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
        date = Date.valueOf(requestDto.getDate());
        start_time = Time.valueOf(requestDto.getStartTime());
        end_time = Time.valueOf(requestDto.getEndTime());
        // TODO: db에 room_name, date 로 reservation 리스트 가져오고 start_time, end_time 비교 ...NoSqlDB적용고려
        List<Reservation> reservations =  reservationRepository.findByRoomNameAndDate(room_name, date);
        if (start_time.compareTo(end_time) >= 0)
            return false;
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
