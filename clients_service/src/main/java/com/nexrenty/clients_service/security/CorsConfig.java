package com.nexrenty.clients_service.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


import java.util.Arrays; 

@Configuration
public class CorsConfig {
    

    /**
     * @see https://docs.spring.io/spring-security/reference/reactive/integrations/cors.html
     * @return
     */
    @Bean
    @Primary
    public CorsConfigurationSource corsConfigSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration() ;

        corsConfiguration.setAllowedOrigins(Arrays.asList("*"));
        corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        corsConfiguration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        // corsConfiguration.setAllowCredentials(true); // Allow cookies and auth

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource() ;
        source.registerCorsConfiguration("/**", corsConfiguration);

        return source ;
    }
}
