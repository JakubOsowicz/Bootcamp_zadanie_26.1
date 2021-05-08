package pl.osowicz.task_manager.User;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.osowicz.task_manager.User.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findAllByActiveIsTrue();
}
