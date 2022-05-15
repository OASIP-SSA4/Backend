package sit.int221.clinicservice.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import sit.int221.clinicservice.dtos.EditEventDTO;
import sit.int221.clinicservice.dtos.EventDTO;
import sit.int221.clinicservice.entities.Event;
import sit.int221.clinicservice.repositories.EventRepository;

import java.util.List;
import java.util.regex.Pattern;

@Service
public class EventService{
    @Autowired
    private EventRepository repository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ListMapper listMapper;
//check email
    private boolean validateEmail(String bookingEmail){
        String pattern = "^(.+)@(\\S+).(\\S+)$";
        return Pattern.compile(pattern).matcher(bookingEmail).matches();
    }
    private void checkBookingEmail(String bookingEmail) {
        if (bookingEmail == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "bookingEmail cannot be null");
        } else if (validateEmail(bookingEmail) == false) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong email format");
        }
    }

    public Event save(EventDTO newEvent) {
        Event e = modelMapper.map(newEvent, Event.class);
        return repository.saveAndFlush(e);
    }

    public List<EventDTO> getAllEvent() {
        List<Event> eventList = repository.findAllByOrderByEventStartTimeDesc();
        return listMapper.mapList(eventList, EventDTO.class, modelMapper);
    }

    public Event save(Event event) {
        checkBookingEmail(event.getBookingEmail());
        return repository.saveAndFlush(event);
    }

    @Autowired
    private EventService(EventRepository repository){
        this.repository = repository;
    }

    public EventDTO getEventById(Integer id) {
        Event event = repository.findById(id)
                .orElseThrow(()->new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Event id "+ id +
                        "Does Not Exist !!!"
                ));
        return modelMapper.map(event, EventDTO.class);
    }
    private EventDTO convertEntityToDto(Event event) {
        EventDTO eventDTO = new EventDTO();
        eventDTO.setId(event.getId());
        eventDTO.setBookingName(event.getBookingName());
        eventDTO.setEventNotes(event.getEventNotes());
        eventDTO.setBookingEmail(event.getBookingEmail());
        eventDTO.setEventStartTime(event.getEventStartTime());
        return eventDTO;
    }

//ทำที่controllerแล้ว
//    public EditEventDTO updateEvent(Event updateEvent, Integer id) {
//        Event editEvent = repository.findById(id).map(event -> {
//            event.setEventNotes(updateEvent.getEventNotes());
//            event.setEventStartTime(updateEvent.getEventStartTime());
//            return event;
//        }).orElseGet(() -> {
//            updateEvent.setId(id);
//            return updateEvent;
//        });
//        repository.saveAndFlush(editEvent);
//        return modelMapper.map(editEvent, EditEventDTO.class);
//    }
}