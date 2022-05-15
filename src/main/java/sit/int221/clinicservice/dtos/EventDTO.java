package sit.int221.clinicservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sit.int221.clinicservice.entities.EventCategory;

import java.time.Instant;
import java.time.LocalTime;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventDTO {
    private Integer id;
    private String bookingName;
    private String bookingEmail;
    private Date eventStartTime;
    private String eventNotes;
    private EventCategoryDTO eventCategory;
    private Integer eventDuration;

//    public Date getEventStartTime() {
//        return eventStartTime;
//    }
//
//    public String getEventNotes() {
//        return eventNotes;
//    }
}