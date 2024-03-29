package sit.int221.clinicservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sit.int221.clinicservice.dtos.EditEventCategoryDTO;
import sit.int221.clinicservice.dtos.EventCategoryDTO;
import sit.int221.clinicservice.entities.EventCategory;
import sit.int221.clinicservice.services.EventCategoryService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/eventCategory")
public class EventCategoryController {
    @Autowired
    EventCategoryService eventCategoryService;

    @Autowired
    EventCategoryService eventService;

    @GetMapping("")
    public List<EventCategoryDTO> getAll(){
        return eventCategoryService.getAllEvent();
    }

    @GetMapping("/{id}")
    public EventCategoryDTO getEventById(@PathVariable Integer id){
        return eventService.getEventById(id);
    }

    @PatchMapping("/{id}")
    public EventCategory updateCategory(@Valid @RequestBody EditEventCategoryDTO EditCategory, @PathVariable Integer id) {
        return eventCategoryService.updateCategory(EditCategory, id);
    }
}
