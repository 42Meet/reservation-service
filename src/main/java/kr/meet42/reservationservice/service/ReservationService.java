package kr.meet42.reservationservice.service;

import com.sun.xml.bind.v2.TODO;
import feign.Response;
import kr.meet42.reservationservice.client.MemberServiceClient;
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
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.criteria.CriteriaBuilder;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.sql.Date;
import java.util.*;
import java.sql.Time;
import java.util.Iterator;

@EnableSwagger2
@RequiredArgsConstructor
@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final MemberRepository memberRepository;
    private final ParticipateRepository participateRepository;
    private final JWTUtil jwtUtil;
    private final MemberServiceClient memberServiceClient;

    @Transactional
    public ResponseEntity<?> save(ReservationSaveRequestDto requestDto, String accessToken) {
        Reservation reservation;
        Integer error_code = 0; // 0 이면 OK, 1이면 시간중복, 2이면 예약가능 횟수 초과

        if (jwtUtil.validateAndExtract(accessToken).compareTo(requestDto.getLeaderName()) != 0)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if (isValid(requestDto, error_code)) {
            requestDto.setStatus(1L);
            reservation = reservationRepository.save(requestDto.toReservationEntity());
            String leaderName = requestDto.getLeaderName();
            if (!requestDto.getMembers().contains(leaderName))
                requestDto.getMembers().add(leaderName);
            for (Iterator<String> iter = requestDto.getMembers().iterator(); iter.hasNext(); ) {
                Member member = requestDto.toMemberEntity(iter.next());
                memberRepository.save(member);
                participateRepository.save(requestDto.toParticipateEntity(reservation, member));
            }
            return new ResponseEntity<>(HttpStatus.CREATED); // 저장 성공
        }
        System.out.println("error_code = " + error_code);
        if (error_code == 1)
            return new ResponseEntity<>(HttpStatus.CONFLICT); // 시간 중복으로 저장 실패
        else if (error_code == 2)
            return new ResponseEntity<>(HttpStatus.FORBIDDEN); // 예약가능 횟수 초과로 저장실패
        else
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 이상한 경우임 Bad request..
    }

    @Transactional // Question: 여기서 Transactional이 필요한가? 그냥 조횐데?
    public ResponseEntity<List<List<ReservationResponseDto>>> findMyReservation(HttpServletRequest request, String accessToken) {
        List<List<ReservationResponseDto>> res = new ArrayList<>(new ArrayList<>());
        List<ReservationResponseDto> proc = new ArrayList<>();
        List<ReservationResponseDto> end = new ArrayList<>();
        List<ReservationResponseDto> sche = new ArrayList<>();
        String intra = jwtUtil.validateAndExtract(accessToken);
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
        return new ResponseEntity(res, HttpStatus.OK);
    }

    @org.springframework.transaction.annotation.Transactional
    public void setReservationStatus(Participate participate) {
        Reservation reservation = participate.getReservation();
        java.util.Date start = new java.util.Date(reservation.getDate().getYear(), reservation.getDate().getMonth(), reservation.getDate().getDate(), reservation.getStartTime().getHours(), 0, 0);
        java.util.Date end = new java.util.Date(reservation.getDate().getYear(), reservation.getDate().getMonth(), reservation.getDate().getDate(), reservation.getEndTime().getHours(), 0, 0);
        java.util.Date cur = Calendar.getInstance().getTime();
        // 예약시간이 더 클경우. 즉, 예정일경우
        if (start.compareTo(cur) > 0) {
            reservation.setStatus(2L);
        }
        // 지났으면
        else if (end.compareTo(cur) < 0) {
            reservation.setStatus(0L);
        }
        else
            reservation.setStatus(1L);
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
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        else {
            // TODO : JWT 연결 되면 주석 풀어서 leader만 삭제할 수 있도록 수정
            if (jwtUtil.validateAndExtract(dto.getAccessToken()).compareTo(finded.get().getLeaderName()) != 0)
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
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

    public boolean isValid(ReservationSaveRequestDto requestDto, Integer error_code) {
        final int MAX_RESERVATION = 2;
        String room_name;
        Date date;
        Time start_time;
        Time end_time;
        Reservation tmp;
        Time tmp_start;
        Time tmp_end;
        int cnt;

        room_name = requestDto.getRoomName();
        date = Date.valueOf(requestDto.getDate());
        start_time = Time.valueOf(requestDto.getStartTime());
        end_time = Time.valueOf(requestDto.getEndTime());
        // TODO: db에 room_name, date 로 reservation 리스트 가져오고 start_time, end_time 비교 ...NoSqlDB적용고려
        List<Reservation> reservations =  reservationRepository.findByRoomNameAndDate(room_name, date);
        cnt = 0;
        if (start_time.compareTo(end_time) >= 0){
            error_code = 1;
            return false;
        }
        for (Iterator<Reservation> iter = reservations.iterator(); iter.hasNext(); ) {
            tmp = iter.next();
            tmp_start = tmp.getStartTime();
            tmp_end = tmp.getEndTime();
            if (Objects.equals(tmp.getLeaderName(), requestDto.getLeaderName()) && tmp.getStatus() != 0) {
                cnt = cnt + 1;
                if (cnt == MAX_RESERVATION) {
                    error_code = 2;
                    return false;
                }
            }
            if ((end_time.compareTo(tmp_start) > 0 && end_time.compareTo(tmp_end) <= 0)
                    || (start_time.compareTo(tmp_end) < 0 && start_time.compareTo(tmp_start) >= 0)
                    || (start_time.compareTo(tmp_start) >= 0 && end_time.compareTo(tmp_end) <= 0)) {
                error_code = 1;
                return false;
            }
        }
        return true;
    }
}
