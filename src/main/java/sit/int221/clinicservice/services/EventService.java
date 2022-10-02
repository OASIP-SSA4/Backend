package sit.int221.clinicservice.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import sit.int221.clinicservice.dtos.CreateEventDTO;
import sit.int221.clinicservice.dtos.EditEventDTO;
import sit.int221.clinicservice.dtos.EventDTO;
import sit.int221.clinicservice.entities.Event;
import sit.int221.clinicservice.entities.User;
import sit.int221.clinicservice.repositories.EventRepository;
import sit.int221.clinicservice.tokens.JwtTokenUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class EventService{
    @Autowired
    private EventRepository repository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ListMapper listMapper;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private MatchService matchService;

    public Event save(CreateEventDTO createEventDTO) {
        Event e = modelMapper.map(createEventDTO, Event.class);
        return repository.saveAndFlush(e);
    }

    public List<EventDTO> getAllEvent() {
        List<Event> eventList = repository.findAllByOrderByEventStartTimeDesc();
        return listMapper.mapList(eventList, EventDTO.class, modelMapper);
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

    public EditEventDTO editEventDTO(EditEventDTO editEventDTO, Integer id) {
        Event event = repository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, id + "does not exist!!!"));
        event.setEventStartTime(editEventDTO.getEventStartTime());
        event.setEventNotes(editEventDTO.getEventNotes());
        repository.saveAndFlush(event);
        return editEventDTO;
    }

    public List<EventDTO> getAll(HttpServletRequest request){
        List<Event> eventsList = repository.findAll(Sort.by(Sort.Direction.DESC,"eventStartTime"));
        String getUserEmail = getUserEmail(getRequestAccessToken(request));
        UserDetails userDetails = matchService.loadUserByUsername(getUserEmail);
        if(userDetails != null && (request.isUserInRole("ROLE_student"))){
            List<Event> eventsListByEmail = repository.findByBookingEmail(getUserEmail);
            return listMapper.mapList(eventsListByEmail, EventDTO.class,modelMapper);

        }
//        else if(userDetails != null && (request.isUserInRole("ROLE_lecturer"))){
////            List<Events> eventsListByEmail = repository.findByBookingEmail(getUserEmail);
//            List<Event> eventsListByCategoryOwner = repository.findEventsCategoryOwnerByEmail(getUserEmail);
//
//            return listMapper.mapList(eventsListByCategoryOwner , EventDTO.class,modelMapper);
//
//        }
        return listMapper.mapList(eventsList, EventDTO.class,modelMapper);
    }

    public String getRequestAccessToken(HttpServletRequest request){
        return request.getHeader("Authorization").substring(7);
    }

    public String getUserEmail(String requestAccessToken){
        return jwtTokenUtil.getUsernameFromToken(requestAccessToken);
    }
}