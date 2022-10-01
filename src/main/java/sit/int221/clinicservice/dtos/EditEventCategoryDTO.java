package sit.int221.clinicservice.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditEventCategoryDTO {
    private String eventCategoryName;
    private String eventCategoryDescription;
    private Integer eventDuration;

    public String getCategoryName() {
        return eventCategoryName;
    }
}
