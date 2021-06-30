package kr.meet42.reservationservice.utils;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;
import java.util.Calendar;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class DateTest {

    @Test
    public void 일요일과월요일찾기() {
        String date = "2021-06-30";

        java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd");
        String dateParts[] = date.split("-");
        String day = dateParts[2];
        String month = dateParts[1];
        String year = dateParts[0];

        Calendar c = Calendar.getInstance();
        c.set(Integer.parseInt(year),Integer.parseInt(month)-1,Integer.parseInt(day));
        int current_day = c.get(Calendar.DAY_OF_WEEK); // 일 월 화 수 목 금 토 순서로 1 2 3 4 5 6 7 값임
        int wanted_day = 2; // 속한 주의 일요일 찾기
        if (current_day != wanted_day) {
            c.add(Calendar.DATE, wanted_day - current_day);
        }
        Date date1 = Date.valueOf(formatter.format(c.getTime()));
        assertThat(date1).isEqualTo(Date.valueOf("2021-06-28"));
        System.out.println("date1 = " + date1);
    }

    @Test
    public void calendar() {
        java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd");
        String date = "2021-06-30";
        String dateParts[] = date.split("-");
        String dd = dateParts[2];
        String MM = dateParts[1];
        String yyyy = dateParts[0];

        Calendar base = Calendar.getInstance();
        System.out.println("base = " + Date.valueOf(formatter.format(base.getTime())));
        base.add(Calendar.DATE, 7);
        Date start = Date.valueOf(formatter.format(base.getTime()));
        System.out.println("start = " + start);

        Calendar d_day = Calendar.getInstance();
        d_day.set(Integer.parseInt(yyyy),Integer.parseInt(MM)-1,Integer.parseInt(dd));
        Date selected = Date.valueOf(formatter.format(d_day.getTime()));
        System.out.println("selected = " + selected);

        if (start.compareTo(selected) > 0)
            System.out.println("예약가능날보다 더 이른 날짜로 예약이 들어온 상황입니다.");

        // 2주 지난날짜가 예약하려는 날짜보다 더 크면안됨. 같거나 작아야함
        base.add(Calendar.DATE, 13);
        Date end = Date.valueOf(formatter.format(base.getTime()));

        if (end.compareTo(selected) < 0)
            System.out.println("예약가능날보다 더 늦은 날짜로 예약이 들어온 상황입니다.");
        System.out.println("end = " + end);
    }
}
