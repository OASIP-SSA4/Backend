package sit.int221.clinicservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sit.int221.clinicservice.entities.Event;

import java.util.Date;
import java.util.List;


public interface EventRepository extends JpaRepository<Event, Integer> {
    List<Event> findAllByOrderByEventStartTimeDesc();
    List<Event> findByBookingEmail(String email);

    @Query(value = "select * from event e where e.bookingName = :bookingName and e.eventStartTime = :eventStartTime",nativeQuery = true)
    List<Event> findConstraintEvent(String bookingName, Date eventStartTime);
}
