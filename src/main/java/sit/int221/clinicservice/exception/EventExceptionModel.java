package sit.int221.clinicservice.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
@Setter
public class EventExceptionModel {
    private HttpStatus status;
    private String message;
    private List<EventFieldError> details;

    public EventExceptionModel(HttpStatus status, String message, List<EventFieldError> detail){
        super();
        this.details = detail;
        this.message = message;
        this.status = status;
    }

    public EventExceptionModel(HttpStatus status, String message, EventFieldError detail){
        super();
        this.status = status;
        this.message = message;
        details = List.of(detail);
    }
}
