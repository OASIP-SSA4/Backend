package sit.int221.clinicservice.validators;

import lombok.*;

import java.util.Date;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class HandleValidationErrors {
    private Date timestamp = new Date();
    private Integer status;
    private String error;
    private String message;
    private String path;
}
