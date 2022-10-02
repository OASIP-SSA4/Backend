package sit.int221.clinicservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import sit.int221.clinicservice.dtos.CreateEventDTO;
import sit.int221.clinicservice.dtos.EditEventDTO;
import sit.int221.clinicservice.dtos.EventDTO;
import sit.int221.clinicservice.entities.Event;
import sit.int221.clinicservice.repositories.EventRepository;
import sit.int221.clinicservice.services.EventService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {
    @Autowired
    EventService eventService;
    @Autowired
    EventRepository eventRepository;

    @GetMapping("")
    public List<EventDTO> getAllEvent(HttpServletRequest httpServletRequest){
        return eventService.getAll(httpServletRequest);
    }

    @GetMapping("/{id}")
    public EventDTO getEventById(@PathVariable Integer id){
        return eventService.getEventById(id);
    }

//create
    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public void Event (@Validated @RequestBody CreateEventDTO createEventDTO) {
        eventService.save (createEventDTO);
    }

//delete
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        eventRepository.findById(id).orElseThrow(()->
                new ResponseStatusException(HttpStatus.NOT_FOUND,
                        id + " does not exist !!!"));
        eventRepository.deleteById(id);
    }

//edit patch
    @PatchMapping("/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public EditEventDTO editEventDTO(@RequestBody EditEventDTO editEventDTO, @PathVariable Integer id) {
        return eventService.editEventDTO(editEventDTO, id);
    }

    private Event mapEvent(Event existingEvent , EditEventDTO updateEvent){
        existingEvent.setEventStartTime(updateEvent.getEventStartTime());
        existingEvent.setEventNotes(updateEvent.getEventNotes());
        return existingEvent;
    }
}
