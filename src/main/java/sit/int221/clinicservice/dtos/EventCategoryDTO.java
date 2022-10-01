package sit.int221.clinicservice.dtos;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class EventCategoryDTO {
    @NotNull(message = "ID cannot null")
    private Integer id;
    @NotBlank(message = "eventCategoryName cannot be empty")
    @Size(max = 100, message = "eventCategoryName Must not exceed 100 characters.")
    private String eventCategoryName;
    private String eventCategoryDescription;
    private Integer eventDuration;
}
