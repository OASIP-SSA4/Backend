package sit.int221.clinicservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sit.int221.clinicservice.entities.Event;

import java.util.List;


public interface EventRepository extends JpaRepository<Event, Integer> {
    List<Event> findAllByOrderByEventStartTimeDesc();
}
