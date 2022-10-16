package sit.int221.clinicservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import sit.int221.clinicservice.dtos.CreateEventDTO;
import sit.int221.clinicservice.dtos.EditEventDTO;
import sit.int221.clinicservice.dtos.EventDTO;
import sit.int221.clinicservice.repositories.EventRepository;
import sit.int221.clinicservice.services.EventService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {
    @Autowired
    EventService eventService;

    @GetMapping("")
    public List<EventDTO> getAllEvent(HttpServletRequest httpServletRequest){
        return eventService.getAll(httpServletRequest);
    }

    @GetMapping("/{id}")
    public EventDTO getEventById(@PathVariable Integer id, HttpServletRequest httpServletRequest){
        return eventService.getEventById(id, httpServletRequest);
    }

//create
    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("!isAuthenticated() or hasAnyRole(\"admin\",\"student\")")
    public void Event (@Validated @RequestBody CreateEventDTO createEventDTO, HttpServletRequest httpServletRequest) {
        eventService.save (createEventDTO, httpServletRequest);
    }

//delete
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id, HttpServletRequest httpServletRequest) {
        eventService.delete(id, httpServletRequest);
    }

//edit patch
    @PatchMapping("/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public EditEventDTO editEventDTO(@RequestBody EditEventDTO editEventDTO, @PathVariable Integer id, HttpServletRequest httpServletRequest) {
        return eventService.editEventDTO(editEventDTO, id, httpServletRequest);
    }
}
