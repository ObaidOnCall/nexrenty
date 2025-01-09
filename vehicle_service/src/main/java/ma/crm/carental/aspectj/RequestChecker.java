package ma.crm.carental.aspectj;

import java.util.ArrayList;
import java.util.List;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import ma.crm.carental.exception.ValidationErrorResponse;
import ma.crm.carental.exception.ValidationException;

@Aspect
@Component
public class RequestChecker {
    

    @Before("@annotation(ma.crm.carental.annotations.ValidateRequest)")

    public void validate(JoinPoint joinPoint) throws ValidationException {

        for (Object arg : joinPoint.getArgs()) {
            if (arg instanceof BindingResult) {
                BindingResult bindingResult = (BindingResult) arg;

                if (bindingResult.hasErrors()) {
                    List<ValidationErrorResponse> errors = new ArrayList<>();

                    bindingResult.getFieldErrors().forEach(fieldError -> {
                        String field = fieldError.getField();
                        String message = fieldError.getDefaultMessage();
                        errors.add(new ValidationErrorResponse(field, message));
                    });

                    throw new ValidationException(errors);
                }
            }
        }
    }
}
