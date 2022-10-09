package sit.int221.clinicservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sit.int221.clinicservice.entities.Event;

import java.util.Date;
import java.util.List;


public interface EventRepository extends JpaRepository<Event, Integer> {
    List<Event> findAllByOrderByEventStartTimeDesc();
    List<Event> findByBookingEmail(String email);

    @Query(value = "select * from event e where e.bookingName = :bookingName and e.eventStartTime = :eventStartTime",nativeQuery = true)
    List<Event> findConstraintEvent(String bookingName, Date eventStartTime);

//    @Query(value = "select e1 from event e1 join userCategory e2 on e1.eventCategory.id = e2.eventCategory.id" +
//            "join user u on u.id = e2.user.id where u.email = :email")

@Query(value = "SELECT e1 FROM Event e1 JOIN EventCategoryOwner e2 ON e1.eventCategory.id = e2.eventCategory.id " +
        "JOIN User u ON u.id = e2.user.id WHERE u.email = :email")
    List<Event> findEventCategoryOwnerByEmail(@Param("email") String email);
}
