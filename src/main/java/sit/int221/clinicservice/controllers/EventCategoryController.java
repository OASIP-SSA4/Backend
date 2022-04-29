package sit.int221.clinicservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import sit.int221.clinicservice.dtos.EventCategoryDTO;
import sit.int221.clinicservice.dtos.EventDTO;
import sit.int221.clinicservice.entities.Event;
import sit.int221.clinicservice.entities.EventCategory;
import sit.int221.clinicservice.repositories.EventCategoryRepository;
import sit.int221.clinicservice.services.EventCategoryService;

import java.util.List;

@RestController
@RequestMapping("/api/eventCategory")
public class EventCategoryController {
    @Autowired
    EventCategoryService eventCategoryService;
    @Autowired
    EventCategoryRepository eventCategoryRepository;

    @GetMapping("")
    public List<EventCategoryDTO> getAllEvent(){
        return eventCategoryService.getAllEvent();
    }
}
