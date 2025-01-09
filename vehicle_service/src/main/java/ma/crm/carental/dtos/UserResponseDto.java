package ma.crm.carental.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data  @NoArgsConstructor @AllArgsConstructor @Builder
@JsonIgnoreProperties(ignoreUnknown = true) 
public class UserResponseDto {
    
    private String id ;
    private String username ;
    private String firstName ;
    private String lastName ;
    private String email ;
    private String emailVerified ;
    private boolean enabled ;
}
