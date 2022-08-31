package sit.int221.clinicservice.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
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

    public List<EventCategoryDTO> getEventCategory() {
        try{
            List<EventCategory> eventList = eventCategoryRepository.findAll(Sort.by("id").descending());
            return listMapper.mapList(eventList, EventCategoryDTO.class, modelMapper);
        } catch (Exception ex){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "ไม่พบข้อมูล");
        }
    }

    public EventCategoryDTO getEventCategoryById(Integer id) {
        EventCategory event = eventCategoryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Event id " + id +
                        "Does Not Exist !!!"
                ));
        return modelMapper.map(event, EventCategoryDTO.class);
    }
}
