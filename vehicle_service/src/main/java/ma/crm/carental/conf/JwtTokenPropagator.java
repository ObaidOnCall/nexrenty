package ma.crm.carental.conf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import feign.RequestInterceptor;
import feign.RequestTemplate;

@Component
public class JwtTokenPropagator implements RequestInterceptor{

    @Override
    public void apply(RequestTemplate template) {
        
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();


        if (principal instanceof Jwt) {
            Jwt jwt = (Jwt) principal;
            // Add the raw token string to the Authorization header
            template.header("Authorization", "Bearer " + jwt.getTokenValue());
        } else {
            throw new IllegalStateException("No JWT token found in SecurityContext");
        }


    }
    
}
