package sit.int221.clinicservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventCategoryDTO {

    @NotNull(message = "ID cannot null")
    private Integer id;
    @NotBlank(message = "eventCategoryName cannot be empty")
    @Size(max = 100, message = "eventCategoryName Must not exceed 100 characters.")
    private String eventCategoryName;
    private String eventCategoryDescription;
    private Integer eventDuration;
}
