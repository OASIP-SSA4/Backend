package sit.int221.clinicservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateEventDTO {
    private Integer id;

    @NotBlank(message = "The booking names is not empty")
    @Size(min = 1, max = 100, message = "The booking names size must be between 1 and 100 characters")
    private String bookingName;

    @NotBlank(message = "Email can not be empty")
    @Size(min = 1, max = 50, message = "Email size is invalied")
    @Email(message = "Email is not valid", regexp = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])")
    private String bookingEmail;

    @NotNull(message = "StartTime can not null")
    @Future(message = "required future date time")
    private Date eventStartTime;

    @Size(min = 0, max = 500, message = "The note size must be between 0 and 500 characters")
    private String eventNotes;
    private EventCategoryDTO eventCategory;
    private Integer eventDuration;
}
