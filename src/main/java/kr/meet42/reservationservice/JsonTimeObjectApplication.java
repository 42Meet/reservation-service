package kr.meet42.reservationservice;

public class JsonTimeObjectApplication {
    public static void main(String[] args) {
        String jsonString = "{\n" +
                "    location: \"개포\",\n" +
                "    room_name: \"창경궁\",\n" +
                "    date: \"2021-06-18\",\n" +
                "    start_time: \"18:00:00\",\n" +
                "    end_time: \"20:00:00\",\n" +
                "    leader_name: \"taehkim\"\n" +
                "    member: {\n" +
                "        1: \"sebaek\",\n" +
                "        2: \"jakang\",\n" +
                "        3: \"esim\"\n" +
                "    }\n" +
                "}";
    }
}
