package pl.osowicz.task_manager.user;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.osowicz.task_manager.user.dtos.UserAddDto;
import pl.osowicz.task_manager.user.dtos.UserEditDto;
import pl.osowicz.task_manager.user.dtos.UserRegistrationDto;

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

    public List<User> getActiveUsers() {
        return userRepository.findAllByDeleted(false);
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

    public User registrationDtoToUser(UserRegistrationDto userDto) {
        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setPassword(encodePassword(userDto.getPassword()));
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
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

    public User addDtoToUser(UserAddDto userAddDto) {
        User user = new User();
        user.setEmail(userAddDto.getEmail());
        user.setFirstName(userAddDto.getFirstName());
        user.setLastName(userAddDto.getLastName());
        Set<UserRole> roleSet = userAddDto.getRoleList().stream()
                .map(role -> new UserRole(user, role))
                .collect(Collectors.toSet());
        user.setRole(roleSet);
        return user;
    }

    public UserEditDto userToEditDto(User user) {
        UserEditDto userEditDto = new UserEditDto();
        userEditDto.setId(user.getId());
        userEditDto.setEmail(user.getEmail());
        userEditDto.setFirstName(user.getFirstName());
        userEditDto.setLastName(user.getLastName());
        List<Role> roleList = user.getRoles().stream()
                .map(UserRole::getRole)
                .collect(Collectors.toList());
        userEditDto.setRoleList(roleList);
        return userEditDto;
    }

    public User editDtoToUser(UserEditDto userEditDto) {
        User user = findById(userEditDto.getId());
        user.setId(userEditDto.getId());
        user.setFirstName(userEditDto.getFirstName());
        user.setLastName(userEditDto.getLastName());
        user.getRoles().clear();
        Set<UserRole> roleSet = userEditDto.getRoleList().stream()
                .map(role -> new UserRole(user, role))
                .collect(Collectors.toSet());
        user.getRoles().addAll(roleSet);
        return user;
    }
}