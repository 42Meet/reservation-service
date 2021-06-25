package kr.meet42.reservationservice.error;

import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class FeignErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        switch (response.status()){
            case 400:
                break;
            case 404:
                if (methodKey.contains("verifyToken")){
                    return new ResponseStatusException(HttpStatus.valueOf(response.status()),
                            "member_service Exception: Token is empty");
                }
                else if (methodKey.contains("getRole")){
                    return new ResponseStatusException(HttpStatus.valueOf(response.status()),
                            "member_service Exception: Role is empty");
                }
            default:
                return new Exception(response.reason());
        }
        return null;
    }
}
