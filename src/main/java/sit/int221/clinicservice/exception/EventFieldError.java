package sit.int221.clinicservice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
@Setter
public class EventFieldError {
    private String field;
    private String errorMessage;
    private HttpStatus status;
}
