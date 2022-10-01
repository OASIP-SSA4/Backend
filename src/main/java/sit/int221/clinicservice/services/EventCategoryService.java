package sit.int221.clinicservice.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import sit.int221.clinicservice.dtos.EditEventCategoryDTO;
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
    public EventCategoryDTO getEventById(Integer id) {
        EventCategory event = eventCategoryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Event id " + id +
                        "Does Not Exist !!!"
                ));
        return modelMapper.map(event, EventCategoryDTO.class);
    }

    public EventCategory updateCategory(EditEventCategoryDTO updateCategory, Integer categoryId) {
        EventCategory existingCategory = eventCategoryRepository.findById(categoryId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, categoryId + " Dose not exits!!!"));
        List<EventCategoryDTO> categoryList = getAllEvent();
        boolean isInvaild = false;
        String exception = "";
        if(updateCategory.getCategoryName() == (null) || updateCategory.getCategoryName().length() == 0){
            exception += "  CategoryName cannot be empty  ";
            isInvaild = true;
        }
        if(updateCategory.getCategoryName().length() > 100){
            exception += "  CategoryName Must not exceed 100 characters.    ";
            isInvaild = true;
        }
        if(!(existingCategory.getEventCategoryName().trim().equalsIgnoreCase(updateCategory.getCategoryName().trim()))){
            for (int i = 0; i < categoryList.size(); i++) {
                if (categoryList.get(i).getEventCategoryName().trim().equalsIgnoreCase(updateCategory.getEventCategoryName().trim())) {
                    exception += "    This CategoryName is overlapping.   ";
                    isInvaild = true;
                }
            }
        }
        if(updateCategory.getEventDuration() < 1 || updateCategory.getEventDuration() > 480){
            exception += "  Duration is must be between 1-480 minutes.   ";
            isInvaild = true;
        }
        if(updateCategory.getEventCategoryDescription().length() > 500){
            exception += "  Description Must not exceed 500 characters. ";
            isInvaild = true;
        }
        if(isInvaild){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception);
        }
        existingCategory.setEventCategoryDescription(updateCategory.getEventCategoryDescription());
        existingCategory.setEventDuration(updateCategory.getEventDuration());
        existingCategory.setEventCategoryName(updateCategory.getEventCategoryName());
        return eventCategoryRepository.saveAndFlush(existingCategory);
    }
}

