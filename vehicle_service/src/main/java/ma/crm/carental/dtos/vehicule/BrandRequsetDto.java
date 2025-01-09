package ma.crm.carental.dtos.vehicule;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter @Setter
public class BrandRequsetDto {
    
    @NotBlank(message = "Name must not be blank")
    @Size(min = 2, max = 64, message = "Name must be between 2 and 64 characters")
    private String name;

    @Size(max = 64, message = "Country of Origin must not exceed 64 characters")
    private String countryOfOrigin;

    @Size(max = 64, message = "Parent Company must not exceed 64 characters")
    private String parentCompany;

    @Pattern(
        regexp = "^(http(s)?://)?(www\\.)?[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}(/\\S*)?$",
        message = "Website must be a valid URL format"
    )
    @Size(max = 128, message = "Website must not exceed 128 characters")
    private String website;
}
