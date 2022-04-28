package sit.int221.clinicservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sit.int221.clinicservice.dtos.EventCategoryDTO;
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
