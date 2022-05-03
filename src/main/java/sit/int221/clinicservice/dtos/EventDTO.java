package sit.int221.clinicservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sit.int221.clinicservice.entities.EventCategory;

import java.time.Instant;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventDTO {
    private Integer id;
    private String bookingName;
    private String bookingEmail;
    private Instant eventStartTime;
    private String eventNotes;
    private EventCategoryDTO eventCategory;
    private Integer eventDuration;
}