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
import sit.int221.clinicservice.entities.EventCategory;
import sit.int221.clinicservice.entities.User;
import sit.int221.clinicservice.repositories.EventCategoryOwnerRepository;
import sit.int221.clinicservice.repositories.EventCategoryRepository;
import sit.int221.clinicservice.repositories.EventRepository;
import sit.int221.clinicservice.repositories.UserRepository;
import sit.int221.clinicservice.tokens.JwtTokenUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
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
    @Autowired
    private UserRepository userRepository;

    public List<EventDTO> getAll(HttpServletRequest request){
        List<Event> eventsList = repository.findAll(Sort.by(Sort.Direction.DESC,"eventStartTime"));
        String getUserEmail = getUserEmail(getRequestAccessToken(request));
        UserDetails userDetails = matchService.loadUserByUsername(getUserEmail);
        if(userDetails != null && (request.isUserInRole("ROLE_student"))){
            List<Event> eventsListByEmail = repository.findByBookingEmail(getUserEmail);
            return listMapper.mapList(eventsListByEmail, EventDTO.class,modelMapper);
        } else if (userDetails != null && (request.isUserInRole("ROLE_lecturer"))){
            List<Event> eventListByCategoryOwner = repository.findEventCategoryOwnerByEmail(getUserEmail);
            return listMapper.mapList(eventListByCategoryOwner , EventDTO.class,modelMapper);
        }
        return listMapper.mapList(eventsList, EventDTO.class,modelMapper);
    }

    public Event save(CreateEventDTO createEventDTO, HttpServletRequest httpServletRequest) {
        Event newEvent = modelMapper.map(createEventDTO, Event.class);
        if (httpServletRequest.isUserInRole("ROLE_student")) {
            String getUserEmail = getEmailFromToken(httpServletRequest);
            User user = userRepository.findByEmail(getUserEmail);
            if((httpServletRequest.isUserInRole("ROLE_student")) && !newEvent.getBookingEmail().equals(user.getEmail())){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"The booking email must be the same as the student's email");
            }
        }
        if ((httpServletRequest.isUserInRole("ROLE_lecturer"))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only student, admin can create event");
        }
        checkConstraints(newEvent.getBookingName(),newEvent.getEventStartTime());
        repository.saveAndFlush(newEvent);
        return repository.saveAndFlush(newEvent);
    }

    @Autowired
    private EventService(EventRepository repository){
        this.repository = repository;
    }

    public EventDTO getEventById(Integer id, HttpServletRequest httpServletRequest) {
        Event event = repository.findById(id)
                .orElseThrow(()->new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Event id "+ id +
                        "Does Not Exist !!!"
                ));
        String getUserEmail = getEmailFromToken(httpServletRequest);
        User user = userRepository.findByEmail(getUserEmail);
        if(user != null) {
            if((httpServletRequest.isUserInRole("ROLE_student")) && !event.getBookingEmail().equals(user.getEmail())) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cannot Edit event which you didn't own");
            }
            if((httpServletRequest.isUserInRole("ROLE_lecturer")) && !event.getBookingEmail().equals(user.getEmail())){
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only student, admin can delete event");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Please Sign in again");
        }
        return modelMapper.map(event, EventDTO.class);
    }

    public EditEventDTO editEventDTO(EditEventDTO editEventDTO, Integer id, HttpServletRequest httpServletRequest) {
        Event event = repository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, id + "does not exist!!!"));
        String getUserEmail = getEmailFromToken(httpServletRequest);
        User user = userRepository.findByEmail(getUserEmail);
        if(user != null) {
            if((httpServletRequest.isUserInRole("ROLE_student")) && !event.getBookingEmail().equals(user.getEmail())) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cannot Edit event which you didn't own");
            }
            if((httpServletRequest.isUserInRole("ROLE_lecturer")) && !event.getBookingEmail().equals(user.getEmail())) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only student, admin can delete event");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Please Sign in again");
        }
        event.setEventStartTime(editEventDTO.getEventStartTime());
        event.setEventNotes(editEventDTO.getEventNotes());
        repository.saveAndFlush(event);
        return editEventDTO;
    }

    public Integer delete(Integer id, HttpServletRequest httpServletRequest) {
        Event event = repository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND,
                    id + " does not exist !!!"));
        String getUserEmail = getEmailFromToken(httpServletRequest);
        User user = userRepository.findByEmail(getUserEmail);
        if(user != null) {
            if((httpServletRequest.isUserInRole("ROLE_student")) && !event.getBookingEmail().equals(user.getEmail())) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cannot delete event which you didn't own");
            }
            if((httpServletRequest.isUserInRole("ROLE_lecturer")) && !event.getBookingEmail().equals(user.getEmail())) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only student, admin can delete event");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Please Sign in again");
        }
        repository.deleteById(id);
        return id;
    }

    public String getRequestAccessToken(HttpServletRequest request){
        return request.getHeader("Authorization").substring(7);
    }

    public String getUserEmail(String requestAccessToken){
        return jwtTokenUtil.getUsernameFromToken(requestAccessToken);
    }

    public String getEmailFromToken(HttpServletRequest httpServletRequest){
        return getUserEmail(getRequestAccessToken(httpServletRequest));
    }

    public void checkConstraints(String bookingName, Date eventStartTime){
        List<Event> constraintEvent = repository.findConstraintEvent(bookingName,eventStartTime);
        if(constraintEvent.size() >= 1){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"name and datetime is already booked");
        }
    }
}