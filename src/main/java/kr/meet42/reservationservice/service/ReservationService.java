package kr.meet42.reservationservice.service;

import com.sun.xml.bind.v2.TODO;
import feign.Response;
import kr.meet42.reservationservice.client.MemberServiceClient;
import kr.meet42.reservationservice.domain.entity.Member;
import kr.meet42.reservationservice.domain.entity.Participate;
import kr.meet42.reservationservice.domain.entity.Reservation;
import kr.meet42.reservationservice.domain.entity.Room;
import kr.meet42.reservationservice.domain.repository.MemberRepository;
import kr.meet42.reservationservice.domain.repository.ParticipateRepository;
import kr.meet42.reservationservice.domain.repository.ReservationRepository;
import kr.meet42.reservationservice.domain.repository.RoomRepository;
import kr.meet42.reservationservice.utils.JWTUtil;
import kr.meet42.reservationservice.web.dto.ReservationPageResponseDto;
import kr.meet42.reservationservice.web.dto.ReservationResponseDto;
import kr.meet42.reservationservice.web.dto.ReservationSaveRequestDto;
import kr.meet42.reservationservice.web.dto.ReservationDeleteRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
import java.awt.print.Pageable;
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
    private final RoomRepository roomRepository;
    private final JWTUtil jwtUtil;
    private final MemberServiceClient memberServiceClient;
    private Integer error_code = 0; // 0이면 ㄱㅊ, 1이면 중복, 2이면 예약가능 횟수 초과

    @Transactional
    public ResponseEntity<?> save(ReservationSaveRequestDto requestDto, String accessToken) {
        Reservation reservation;

        Optional<Room> room = roomRepository.findByLocationAndRoomName(requestDto.getLocation(), requestDto.getRoomName());
        if (!room.isPresent())
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        if (jwtUtil.validateAndExtract(accessToken).compareTo(requestDto.getLeaderName()) != 0)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if (isTimeValid(requestDto) && isCntValid(requestDto)) {
            // TODO: 승인했으면 setStatus를 통해 적절한 값을 지정해 주어야 한다.
            // status를 일단 승인대기상태로
            requestDto.setStatus(3L);
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
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 이상한 경우임 NOT_FOUND..
    }

    @Transactional // Question: 여기서 Transactional이 필요한가? 그냥 조횐데?
    public ResponseEntity<List<List<ReservationResponseDto>>> findMyReservation(HttpServletRequest request, String accessToken) {
        List<List<ReservationResponseDto>> res = new ArrayList<>(new ArrayList<>());
        List<ReservationResponseDto> proc = new ArrayList<>();
        List<ReservationResponseDto> end = new ArrayList<>();
        List<ReservationResponseDto> sche = new ArrayList<>();
        List<ReservationResponseDto> wait = new ArrayList<>();

        String intra = jwtUtil.validateAndExtract(accessToken);
        List<Member> members = memberRepository.findByIntra(intra);
        for (Iterator<Member> iter = members.iterator(); iter.hasNext();) {
            Member member = iter.next();
            Participate participate = participateRepository.findByMember(member);
            Reservation reservation = participate.getReservation();
            setReservationStatus(reservation);
            Optional<Reservation> expected = reservationRepository.findById(reservation.getId());
            if (expected.isPresent()) {
                Long status = expected.get().getStatus();
                if (status == 1L)
                    proc.add(expected.get().toResponseDto(getMembers(expected.get())));
                else if (status == 2L)
                    sche.add(expected.get().toResponseDto(getMembers(expected.get())));
                else if (status == 0L)
                    end.add(expected.get().toResponseDto(getMembers(expected.get())));
                else if (status == 3L)
                    wait.add(expected.get().toResponseDto(getMembers(expected.get())));
            }
        }
        listAscSort(proc);
        listAscSort(sche);
        listDescSort(end);
        listAscSort(wait);
        res.add(proc);
        res.add(sche);
        res.add(end);
        res.add(wait);
        return new ResponseEntity(res, HttpStatus.OK);
    }

//    @Transactional
//    public ResponseEntity<Page<Reservation>> findMyWaitingReservation(HttpServletRequest request, Pageable pageable, String accessToken) {
//        List<Reservation> wait = new ArrayList<>();
//
//        String intra = jwtUtil.validateAndExtract(accessToken);
//        List<Member> members = memberRepository.findByIntra(intra);
//        for (Iterator<Member> iter = members.iterator(); iter.hasNext();) {
//            Member member = iter.next();
//            Participate participate = participateRepository.findByMember(member);
//            Reservation reservation = participate.getReservation();
//            setReservationStatus(reservation, intra);
//            Optional<Reservation> expected = reservationRepository.findById(reservation.getId());
//            if (expected.isPresent()) {
//                Long status = expected.get().getStatus();
//                if (status == 3L)
//                    wait.add(expected.get());
//            }
//        }
//        final Page<Reservation> page = new PageImpl<>(wait);
//        return new ResponseEntity(page, HttpStatus.OK);
//    }

//    @Transactional
//    public ResponseEntity<Page<ReservationResponseDto>> pageMyReservationByStatus(HttpServletRequest request, Pageable pageable, Long myStatus, String accessToken) {
//        List<ReservationResponseDto> result = new ArrayList<>();
//
//        String intra = jwtUtil.validateAndExtract(accessToken);
//        List<Member> members = memberRepository.findByIntra(intra);
//        for (Member member : members) {
//            Participate participate = participateRepository.findByMember(member);
//            Reservation reservation = participate.getReservation();
//            setReservationStatus(reservation);
//            Optional<Reservation> expected = reservationRepository.findById(reservation.getId());
//            if (expected.isPresent()) {
//                Long status = expected.get().getStatus();
//                if (status == myStatus)
//                    result.add(expected.get().toResponseDto(getMembers(expected.get())));
//            }
//        }
//        listAscSort(result);
//
//        final Page<ReservationResponseDto> page = new PageImpl<>(result);
//        return new ResponseEntity(page, HttpStatus.OK);
//    }


    @Transactional // Question: 여기서 Transactional이 필요한가? 그냥 조횐데?
    public ResponseEntity<List<ReservationResponseDto>> findMyReservationByStatus(HttpServletRequest request, Long myStatus, String accessToken) {
        List<ReservationResponseDto> result = new ArrayList<>();

        String intra = jwtUtil.validateAndExtract(accessToken);
        List<Member> members = memberRepository.findByIntra(intra);
        for (Member member : members) {
            Participate participate = participateRepository.findByMember(member);
            Reservation reservation = participate.getReservation();
            setReservationStatus(reservation);
            Optional<Reservation> expected = reservationRepository.findById(reservation.getId());
            if (expected.isPresent()) {
                Long status = expected.get().getStatus();
                if (status == myStatus)
                    result.add(expected.get().toResponseDto(getMembers(expected.get())));
            }
        }
        listAscSort(result);
        return new ResponseEntity(result, HttpStatus.OK);
    }

    @Transactional // Question: 여기서 Transactional이 필요한가? 그냥 조횐데?
    public ResponseEntity<ReservationPageResponseDto> pageMyReservationByStatus(int currentPage, int pageBlock, HttpServletRequest request, Long myStatus, String accessToken) {
        List<ReservationResponseDto> result = new ArrayList<>();
        List<ReservationResponseDto> shown = new ArrayList<>();
        ReservationPageResponseDto reservationPageResponseDto;
        String intra = jwtUtil.validateAndExtract(accessToken);
        List<Member> members = memberRepository.findByIntra(intra);
        for (Member member : members) {
            Participate participate = participateRepository.findByMember(member);
            Reservation reservation = participate.getReservation();
            setReservationStatus(reservation);
            Optional<Reservation> expected = reservationRepository.findById(reservation.getId());
            if (expected.isPresent()) {
                Long status = expected.get().getStatus();
                if (status == myStatus)
                    result.add(expected.get().toResponseDto(getMembers(expected.get())));
            }
        }
        if (myStatus == 1L || myStatus == 2L)
            listAscSort(result);
        else if (myStatus == 0L)
            listDescSort(result);
        // 100개있다고 쳐, 2번째 페이지 보고싶대. 한페이지당 10개야
        // 총페이지수 = 전체 개수 / 10
        // 시작글: (2-1) * 10 이고, 끝글: (2) * 10 - 1
        int len = result.size();
        if (pageBlock < 1)
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        int maxPage;
        if (len % pageBlock != 0)
            maxPage = len / pageBlock + 1;
        else if (len / pageBlock == 0)
            maxPage = 0;
        else
            maxPage = len / pageBlock;
        if (maxPage == 0) {
            if (currentPage < 1 || currentPage > (maxPage + 1))
                return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        else
            if (currentPage < 1 || currentPage > maxPage)
                return new ResponseEntity(HttpStatus.NO_CONTENT);
        if ((currentPage * pageBlock) > len)
            shown = result.subList((currentPage-1) * pageBlock, len);
        else
            shown = result.subList((currentPage-1) * pageBlock, currentPage * pageBlock);
        reservationPageResponseDto = ReservationPageResponseDto.builder()
                .currentPage(currentPage)
                .maxPage(maxPage)
                .reservationResponseDtos(shown)
                .build();
        return new ResponseEntity(reservationPageResponseDto, HttpStatus.OK);
    }


    @org.springframework.transaction.annotation.Transactional
    public void setReservationStatus(Reservation reservation) {
        java.util.Date start = new java.util.Date(reservation.getDate().getYear(), reservation.getDate().getMonth(), reservation.getDate().getDate(), reservation.getStartTime().getHours(), 0, 0);
        java.util.Date end = new java.util.Date(reservation.getDate().getYear(), reservation.getDate().getMonth(), reservation.getDate().getDate(), reservation.getEndTime().getHours(), 0, 0);
        java.util.Date cur = Calendar.getInstance().getTime();
        if (reservation.getStatus() >= 3L)
            return ;
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


    public List<ReservationResponseDto> getReservationResponseDtos(List<Reservation> resRaw) {
        List<ReservationResponseDto> res = new ArrayList<>();
        resRaw.iterator().forEachRemaining(reservation -> {res.add(reservation.toResponseDto(getMembers(reservation)));});
        return res;
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

    public boolean isCntValid(ReservationSaveRequestDto requestDto){
        final int MAX_RESERVATION = 100;
        String date = requestDto.getDate();
        Date sunday;
        Date saturday;
        // TODO: date기준 일욜-월욜 찾아서 countbybetween쿼리 날리기. 1주당 예약이 MAX_RESERVATION이상이면 안됨
        sunday = findDay(date, 1);
        saturday = findDay(date, 7);
        if (reservationRepository.countByLeaderNameAndDateBetweenAndStatusIsNot(requestDto.getLeaderName(), sunday, saturday, 4L) >= MAX_RESERVATION) {
            if (error_code != 1)
                error_code = 2;
            return false;
        }
        return true;
    }

    // USAGE: 예약할 날짜, 찾고싶은 날짜의 요일 코드번호(일~토 순서로 1~7)
    public Date findDay(String date, int wanted_day) {
        java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd");
        String dateParts[] = date.split("-");
        String dd = dateParts[2];
        String MM = dateParts[1];
        String yyyy = dateParts[0];

        Calendar c = Calendar.getInstance();
        c.set(Integer.parseInt(yyyy),Integer.parseInt(MM)-1,Integer.parseInt(dd));
        int current_day = c.get(Calendar.DAY_OF_WEEK);
        if (current_day != wanted_day) {
            c.add(Calendar.DATE, wanted_day - current_day);
        }
        return Date.valueOf(formatter.format(c.getTime()));
    }

    public boolean checkDate(ReservationSaveRequestDto requestDto) {
        String dateParts[] = requestDto.getDate().split("-");
        String dd = dateParts[2];
        String MM = dateParts[1];
        String yyyy = dateParts[0];
        java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd");

        Calendar base = Calendar.getInstance();
        base.add(Calendar.DATE, 7);
        Date start = Date.valueOf(formatter.format(base.getTime()));
        Calendar selected_cal = Calendar.getInstance();
        selected_cal.set(Integer.parseInt(yyyy),Integer.parseInt(MM)-1,Integer.parseInt(dd));
        Date selected = Date.valueOf(formatter.format(selected_cal.getTime()));
        base.add(Calendar.DATE, 13);
        Date end = Date.valueOf(formatter.format(base.getTime()));
        if ((start.compareTo(selected) > 0) || (end.compareTo(selected) < 0))
            return false;
        else
            return true;
    }

    public boolean isTimeValid(ReservationSaveRequestDto requestDto) {
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
        List<Reservation> reservations =  reservationRepository.findByRoomNameAndDateAndStatusIsNot(room_name, date, 4L);
        if (start_time.compareTo(end_time) >= 0 || !checkDate(requestDto)){
            error_code = 1;
            return false;
        }
        for (Iterator<Reservation> iter = reservations.iterator(); iter.hasNext(); ) {
            tmp = iter.next();
            tmp_start = tmp.getStartTime();
            tmp_end = tmp.getEndTime();
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
