package sit.int221.clinicservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sit.int221.clinicservice.entities.EventCategory;

public interface EventCategoryRepository extends JpaRepository<EventCategory, Integer> {

}
