package sit.int221.clinicservice.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import sit.int221.clinicservice.dtos.EventDTO;
import sit.int221.clinicservice.entities.Event;
import sit.int221.clinicservice.repositories.EventRepository;

import java.util.List;

@Service
public class EventService{
    @Autowired
    private EventRepository repository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ListMapper listMapper;

    public Event save(EventDTO newEvent) {
        Event e = modelMapper.map(newEvent, Event.class);
        return repository.saveAndFlush(e);
    }

    public List<EventDTO> getAllEvent() {
        List<Event> eventList = repository.findAllByOrderByEventStartTimeDesc();
        return listMapper.mapList(eventList, EventDTO.class, modelMapper);
    }
    public Event save(Event event) {
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
}