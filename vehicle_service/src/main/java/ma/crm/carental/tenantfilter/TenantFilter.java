// package ma.crm.carental.tenantfilter;

// import java.io.IOException;

// import org.springframework.security.core.Authentication;
// import org.springframework.security.core.context.SecurityContextHolder;
// import org.springframework.security.oauth2.jwt.Jwt;
// import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
// import org.springframework.stereotype.Component;

// import jakarta.servlet.Filter;
// import jakarta.servlet.FilterChain;
// import jakarta.servlet.ServletException;
// import jakarta.servlet.ServletRequest;
// import jakarta.servlet.ServletResponse;
// import lombok.extern.slf4j.Slf4j;


// @Component
// @Slf4j
// public class TenantFilter implements Filter{

//     @Override
//     public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
//             throws IOException, ServletException {


//         Authentication authentication = SecurityContextHolder.getContext().getAuthentication() ;
        
        
//         if (authentication instanceof JwtAuthenticationToken) {

//             TenantContext.setTenantId(
//                (String)((Jwt) authentication.getPrincipal())
//                 .getClaims().get("sub")
//             );
//         }

//         chain.doFilter(request, response);
//     }
    
// }
