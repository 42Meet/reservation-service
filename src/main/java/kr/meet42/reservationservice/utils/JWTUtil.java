package kr.meet42.reservationservice.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.impl.DefaultClaims;
import io.jsonwebtoken.impl.DefaultJws;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class JWTUtil {
    private long expire = 60 * 24; // 1day
    private Environment env;
    private String secretKey;

    @Autowired
    public JWTUtil(Environment env) {
        this.env = env;
    }

    public String validateAndExtract(String tokenStr) {
        String contentValue = null;

        try {
            secretKey = env.getProperty("token.secret");
            DefaultJws defaultJws = (DefaultJws) Jwts.parser().setSigningKey(secretKey.getBytes("UTF-8")).parseClaimsJws(tokenStr);
            log.info(defaultJws);
            log.info(defaultJws.getBody().getClass());
            DefaultClaims claims = (DefaultClaims) defaultJws.getBody();
            contentValue = claims.getSubject();
        } catch(Exception e){
            e.printStackTrace();
            log.error(e.getMessage());
            contentValue = null;
        }
        return contentValue;
    }
}
