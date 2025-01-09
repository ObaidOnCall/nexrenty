package ma.crm.carental.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class UserCreateDto {

    @NotBlank(message = "username is required.")
    @Size(min = 6)
    private String username ;

    private String firstName ;
    private String lastName ;

    @NotBlank(message = "email is required.")
    @Email(message = "Please enter a valid email address.")
    private String email ;

    @Builder.Default
    private boolean emailVerified = false;

    @Builder.Default
    private boolean enabled = true;
}
