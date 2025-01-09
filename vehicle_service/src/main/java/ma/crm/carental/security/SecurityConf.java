package ma.crm.carental.security;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfigurationSource;

import ma.crm.carental.conf.KeycloakJwtAuthenticationConverter;

import org.springframework.beans.factory.annotation.Value;

import java.security.KeyFactory;
import java.util.Base64;

import org.springframework.core.io.ClassPathResource;


@Configuration
@EnableWebSecurity
public class SecurityConf {
    
    private final CorsConfigurationSource corsConfigurationSource ;

    // @Value("${spring.security.oauth2.resourceserver.jwt.public-key-location}")
    // private Resource publicKeyResource;

    @Value("${spring.security.oauth2.resourceserver.jwt.jws-algorithms}")
    private String jwsAlgorithm;


    private static final String[] AUTH_WHITELIST = {
        // -- Swagger UI v2
        "/v2/api-docs",
        "/swagger-resources",
        "/swagger-resources/**",
        "/configuration/ui",
        "/swagger-ui.html",
        "/webjars/**",
        // -- Swagger UI v3 (OpenAPI)
        "/v3/api-docs/**",
        "/swagger-ui/**" ,
        /** actuator */
        "/actuator/**" ,

        /** */
        "/agency/**" ,
        "/subscriptions/types",
        "/tests/**",
        /**
         * ! temporoy
         */
        "/users/test-user/**" ,

        // other public endpoints of your API may be appended to this array
    };

    @Autowired
    SecurityConf(
        CorsConfigurationSource corsConfigurationSource 
        ) {
        this.corsConfigurationSource = corsConfigurationSource ;
    }

    @Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http ) throws Exception {
        
        http
        .cors(cors -> cors.configurationSource(corsConfigurationSource))
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests( authorize  ->  {
                authorize.requestMatchers(AUTH_WHITELIST).permitAll() ;
                // authorize.requestMatchers("/**").hasRole("USER") ;
                authorize.anyRequest().authenticated() ;
            }
        )
        .oauth2ResourceServer( oauth2 ->
            oauth2.jwt( jwt ->
                {
                    jwt.jwtAuthenticationConverter(new KeycloakJwtAuthenticationConverter()) ;
                    jwt.decoder(jwtDecoder()) ;

                }
            )
        )
        .sessionManagement(sessionManagement -> 
            sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        )
        .formLogin(AbstractHttpConfigurer::disable)
        .httpBasic(AbstractHttpConfigurer::disable);

        return http.build() ;
    }


    @Bean
    public JwtDecoder jwtDecoder() {
        try {
            // Read the public key from the specified location
            // String publicKeyPEM = new String(Files.readAllBytes(publicKeyResource.getFile().toPath()));
            ClassPathResource publicKeyResource = new ClassPathResource("my-key.pub");



            String publicKeyPEM;
            try (InputStream inputStream = publicKeyResource.getInputStream()) {
                publicKeyPEM = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            }

            /**
             * ! do not do it in producation env
             */

                // String publicKeyPEM = "-----BEGIN PUBLIC KEY-----\n" + //
                //         "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAt7UVwQPra71PcAFzmr1g9XP/iT2CCaOE40n95KGAqS2TMvuX7wJfHZ+K0IkI3lOjFVDu7vpW/JzsK2vGdKadY1mPdYWcjlaSwJWdVSnzVkqI5J0NDLTOg8P+RlbB80myVX+pFeRqtcEy8zUKraxTZhZdAv28VkzG0N3+gFqwNHb+fa1Cmp8tWk314M+UtyktS9ZxEGXdmJaNwncRCzv6Cr0bJJRtpAJtipp2yAcgeLfqAEcjkwSoE+msRkhVoflV4IWLH81e+pryeDeQGnNsAukBg6RPH4qpS66JKjFsQPiEffUYUDh+hWXxxNc6PPZMmVGMIEl2s9ocl4fw4fs+rQIDAQAB\n" + //
                //         "-----END PUBLIC KEY-----" ;
            /**
             * !
            */
            publicKeyPEM = publicKeyPEM.replace("-----BEGIN PUBLIC KEY-----", "")
                                    .replace("-----END PUBLIC KEY-----", "")
                                    .replaceAll("\\s+", "");
            byte[] encoded = Base64.getDecoder().decode(publicKeyPEM);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encoded);
            PublicKey publicKey = keyFactory.generatePublic(keySpec);
            
            // Create and return the JwtDecoder
            return NimbusJwtDecoder.withPublicKey((RSAPublicKey) publicKey).build();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create JWT decoder", e);
        }
    }

}