package ma.crm.carental.exception;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;


@EqualsAndHashCode(callSuper=false)
@Data @AllArgsConstructor
public class ValidationException extends RuntimeException{

    private final transient List<ValidationErrorResponse> errors;
    
}
