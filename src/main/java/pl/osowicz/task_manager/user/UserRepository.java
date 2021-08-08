package pl.osowicz.task_manager.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.roles WHERE u.deleted = ?1")
    List<User> findAllByDeleted(boolean deleted);

    Optional<User> findAllByEmail(String email);

    Optional<User> findByPasswordResetKey(String key);
}
