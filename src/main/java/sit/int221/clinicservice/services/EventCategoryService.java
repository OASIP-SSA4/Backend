package sit.int221.clinicservice.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sit.int221.clinicservice.dtos.EventCategoryDTO;
import sit.int221.clinicservice.entities.EventCategory;
import sit.int221.clinicservice.repositories.EventCategoryRepository;

import java.util.List;

@Service
public class EventCategoryService {
    @Autowired
    private ListMapper listMapper;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private EventCategoryRepository eventCategoryRepository;

    public List<EventCategoryDTO> getAllEvent() {
        List<EventCategory> eventList = eventCategoryRepository.findAll();
        return listMapper.mapList(eventList, EventCategoryDTO.class, modelMapper);
    }
}
