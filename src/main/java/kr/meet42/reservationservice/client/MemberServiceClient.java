package kr.meet42.reservationservice.client;

import kr.meet42.reservationservice.web.dto.TokenDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@FeignClient(name = "member-service")
public interface MemberServiceClient {
    
    @GetMapping("/{username}/role")
    String getRole(@PathVariable("username") String username);
}
