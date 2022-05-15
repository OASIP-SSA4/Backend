package sit.int221.clinicservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import sit.int221.clinicservice.dtos.EditEventDTO;
import sit.int221.clinicservice.dtos.EventDTO;
import sit.int221.clinicservice.entities.Event;
import sit.int221.clinicservice.repositories.EventRepository;
import sit.int221.clinicservice.services.EventService;

import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {
    @Autowired
    EventService eventService;
    @Autowired
    EventRepository eventRepository;

    @GetMapping("")
    public List<EventDTO> getAllEvent(){
        return eventService.getAllEvent();
    }

    @GetMapping("/{id}")
    public EventDTO getEventById(@PathVariable Integer id){
        return eventService.getEventById(id);
    }

//create
    @PostMapping("")
    public void Event (@RequestBody Event event) {
        eventService.save (event);
    }

//delete
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        eventRepository.findById(id).orElseThrow(()->
                new ResponseStatusException(HttpStatus.NOT_FOUND,
                        id + " does not exist !!!"));
        eventRepository.deleteById(id);
    }

////edit put
//    @PutMapping("/{id}")
//    public Event update(@RequestBody Event updateEvent, @PathVariable Integer id) {
//        Event event = eventRepository.findById(id)
//                .map(o->mapEvent(o, updateEvent))
//                .orElseGet(()-> {
//                    updateEvent.setId(id);
//                    return updateEvent;
//                });
//        return eventRepository.saveAndFlush(event);
//    }
//
//    private Event mapEvent(Event existingEvent , Event updateEvent){
//        existingEvent.setBookingEmail(updateEvent.getBookingEmail());
//        existingEvent.setEventCategory(updateEvent.getEventCategory());
//        existingEvent.setBookingName(updateEvent.getBookingName());
//        existingEvent.setEventDuration(updateEvent.getEventDuration());
//        existingEvent.setEventStartTime(updateEvent.getEventStartTime());
//        existingEvent.setEventNotes(updateEvent.getEventNotes());
//        return existingEvent;
//    }

//edit patch
    @PatchMapping("/{id}")
    public Event update(@RequestBody EditEventDTO updateEvent, @PathVariable Integer id) {
        Event event = eventRepository.findById(id)
                .map(o -> mapEvent(o, updateEvent))
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "ไม่พบ id เบอร์" + id
                ));
        return eventRepository.saveAndFlush(event);
    }

    private Event mapEvent(Event existingEvent , EditEventDTO updateEvent){
        existingEvent.setEventStartTime(updateEvent.getEventStartTime());
        existingEvent.setEventNotes(updateEvent.getEventNotes());
        return existingEvent;
    }
}
