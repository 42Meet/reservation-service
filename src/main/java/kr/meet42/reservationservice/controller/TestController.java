package kr.meet42.reservationservice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class TestController {

    @GetMapping("/good")
    public void good(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("request = " + request);
    }

}