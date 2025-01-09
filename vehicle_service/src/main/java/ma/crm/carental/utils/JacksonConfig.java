package ma.crm.carental.utils;


import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration  
public class JacksonConfig {
    

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer addCustomJacksonModules() {
        return jacksonObjectMapperBuilder -> jacksonObjectMapperBuilder.modulesToInstall(JavaTimeModule.class);
    }
}
