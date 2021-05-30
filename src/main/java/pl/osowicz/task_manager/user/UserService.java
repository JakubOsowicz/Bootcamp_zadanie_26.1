package pl.osowicz.task_manager.user;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getActiveUsers() {
        return userRepository.findAllByDeleted(false);
    }

    void save(User user) {
        userRepository.save(user);
    }

    User findById(Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.orElse(null);
    }

    void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    void safeDelete(Long id, User user) {
        if (user.getTaskList().isEmpty()) {
            deleteById(id);
        } else {
            user.setDeleted(true);
            save(user);
        }
    }
}