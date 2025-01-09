package ma.crm.carental.dtos;

import java.util.Optional;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor @Builder 
public class UserUpdateDto {
    private String firstName ;
    private String lastName ;
    
    @Email(message = "Please enter a valid email address.")
    private String email ;

    private Boolean emailVerified ;
    private Boolean enabled ;
}
