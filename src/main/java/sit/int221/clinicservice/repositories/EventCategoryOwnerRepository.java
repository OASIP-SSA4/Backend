package sit.int221.clinicservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sit.int221.clinicservice.entities.EventCategoryOwner;

public interface EventCategoryOwnerRepository extends JpaRepository<EventCategoryOwner, Integer> {
}
