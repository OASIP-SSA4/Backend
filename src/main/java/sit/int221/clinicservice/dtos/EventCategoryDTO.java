package sit.int221.clinicservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventCategoryDTO {
    private Integer id;
    private String eventCategoryName;
    private String eventCategoryDescription;
    private Integer eventDuration;
}
