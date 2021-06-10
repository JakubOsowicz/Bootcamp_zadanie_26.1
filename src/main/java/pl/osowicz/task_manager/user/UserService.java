package pl.osowicz.task_manager.user;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.osowicz.task_manager.user.dtos.*;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UserDto> getActiveUsersFullDto() {
        return userRepository.findAllByDeleted(false)
                .stream()
                .map(user -> new UserDto(user.getId(), user.getEmail(), user.getFirstName(), user.getLastName(), user.getRoles()))
                .collect(Collectors.toList());
    }

    public List<UserDto> getActiveUsersDto() {
        return userRepository.findAllByDeleted(false)
                .stream()
                .map(user -> new UserDto(user.getId(), user.getFirstName(), user.getLastName()))
                .collect(Collectors.toList());
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public User findById(Long id) {
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

    public User registerDtoToUser(UserFrontDto userFrontDto) {
        String email = userFrontDto.getEmail();
        String password = encodePassword(userFrontDto.getPassword());
        String firstName = userFrontDto.getFirstName();
        String lastName = userFrontDto.getLastName();
        User user = new User(email, password, firstName, lastName);
        setDefaultRole(user);
        return user;
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    private void setDefaultRole(User user) {
        List<UserRole> roleList = Collections.singletonList(new UserRole(user, Role.ROLE_GUEST));
        user.setRole(new HashSet<>(roleList));
    }

    public User addFrontDtoToUser(UserFrontDto userFrontDto) {
        String email = userFrontDto.getEmail();
        String firstName = userFrontDto.getFirstName();
        String lastName = userFrontDto.getLastName();
        User user = new User(email, firstName, lastName);
        Set<UserRole> roleSet = userFrontDto.getRoleList()
                .stream()
                .map(role -> new UserRole(user, role))
                .collect(Collectors.toSet());
        user.setRole(roleSet);
        return user;
    }

    public UserFrontDto userToFrontDto(User user) {
        Long id = user.getId();
        String email = user.getEmail();
        String firstName = user.getFirstName();
        String lastName = user.getLastName();
        List<Role> roleList = user.getRoles()
                .stream()
                .map(UserRole::getRole)
                .collect(Collectors.toList());
        return new UserFrontDto(id, email, firstName, lastName, roleList);
    }

    public User frontDtoToUser(UserFrontDto userFrontDto) {
        User user = findById(userFrontDto.getId());
        user.setId(userFrontDto.getId());
        user.setFirstName(userFrontDto.getFirstName());
        user.setLastName(userFrontDto.getLastName());
        user.getRoles().clear();
        Set<UserRole> roleSet = userFrontDto.getRoleList()
                .stream()
                .map(role -> new UserRole(user, role))
                .collect(Collectors.toSet());
        user.getRoles().addAll(roleSet);
        return user;
    }
}