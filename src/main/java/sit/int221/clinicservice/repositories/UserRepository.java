package sit.int221.clinicservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sit.int221.clinicservice.entities.User;

import java.util.List;


public interface UserRepository extends JpaRepository<User, Integer> {
    List<User> findAll();

    @Query(value = "select * from user s where s.name = :userName",nativeQuery = true)
    List<User> findConstraintNameCreate(String userName);
    @Query(value = "select * from user s where s.email = :email",nativeQuery = true)
    List<User> findConstraintEmailCreate(String email);
    @Query(value = "select * from user s where s.name = :userName and s.userId != :id ",nativeQuery = true)
    List<User> findConstraintNameUpdate(String userName,Integer id);
    @Query(value = "select * from user s where s.email = :email and s.userId != :id",nativeQuery = true)
    List<User> findConstraintEmailUpdate(String email,Integer id);
    @Query(value = "select * from user s where s.name = :userName",nativeQuery = true)
    User findUserFromName(String userName);
    @Query(value = "select * from user s where s.email = :email",nativeQuery = true)
    User findUserFromEmail(String email);
}
