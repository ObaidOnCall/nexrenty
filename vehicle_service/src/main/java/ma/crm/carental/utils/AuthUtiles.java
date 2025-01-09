package ma.crm.carental.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

public class AuthUtiles {
    
    private AuthUtiles () {}


    public static String getUsername() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        // Check if the authentication is valid and the user is authenticated
        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof Jwt) {
            Jwt jwt = (Jwt) authentication.getPrincipal();
            return jwt.getClaimAsString("preferred_username");
        }

        return null;
    }
}
