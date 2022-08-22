package sit.int221.clinicservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sit.int221.clinicservice.entities.User;


public interface UserRepository extends JpaRepository<User, Integer> {
    Boolean existsUsersByEmail (String email);
}
