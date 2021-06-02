package pl.osowicz.task_manager.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findAllByDeleted(boolean deleted);

    Optional<User> findAllByEmail(String email);
}
