package pl.osowicz.task_manager.user;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import pl.osowicz.task_manager.mail.MailSenderService;
import pl.osowicz.task_manager.task.Task;
import pl.osowicz.task_manager.user.dtos.UserDto;
import pl.osowicz.task_manager.user.dtos.UserFrontDto;

import javax.mail.MessagingException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailSenderService mailSenderService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, MailSenderService mailSenderService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailSenderService = mailSenderService;
    }

    public User findByEmail(String email) {
        Optional<User> user = userRepository.findAllByEmail(email);
        return user.orElseThrow(() -> new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    public User findById(Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.orElseThrow(() -> new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    public List<UserDto> getActiveUsersFullDto() {
        return userRepository.findAllByDeleted(false)
                .stream()
                .map(user -> new UserDto(user.getId(), user.getEmail(), user.getFirstName(), user.getLastName(), user.getRoles()))
                .collect(Collectors.toList());
    }

    public List<UserDto> getActiveUsersSimpleDto() {
        return userRepository.findAllByDeleted(false)
                .stream()
                .map(user -> new UserDto(user.getId(), user.getFirstName(), user.getLastName()))
                .collect(Collectors.toList());
    }

    public void save(User user) {
        userRepository.save(user);
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

    public void registerNewUser(UserDto userDto) {
        User user = dtoToUser(userDto);
        user.setPassword(encodePassword(user.getPassword()));
        user.setRole(createDefaultRoleSet(user));
        save(user);
    }

    public User dtoToUser(UserDto userDto) {
        String email = userDto.getEmail();
        String password = userDto.getPassword();
        String firstName = userDto.getFirstName();
        String lastName = userDto.getLastName();
        return new User(email, password, firstName, lastName);
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    private Set<UserRole> createDefaultRoleSet(User user) {
        Set<UserRole> roleSet;
        if (user.getRoles() == null) {
            roleSet = new HashSet<>();
        } else {
            roleSet = user.getRoles();
        }
        roleSet.add(new UserRole(user, Role.ROLE_UNCONFIRMED));
        return roleSet;
    }

    public User addFrontDtoToUser(UserFrontDto userFrontDto) {
        String email = userFrontDto.getEmail();
        String firstName = userFrontDto.getFirstName();
        String lastName = userFrontDto.getLastName();
        User user = new User(email, firstName, lastName);
        Set<UserRole> roleSet = userRoleListToSet(userFrontDto, user);
        user.setRole(roleSet);
        return user;
    }

    private Set<UserRole> userRoleListToSet(UserFrontDto userFrontDto, User user) {
        Set<UserRole> roleSet = userFrontDto.getRoleList()
                .stream()
                .map(role -> new UserRole(user, role))
                .collect(Collectors.toSet());
        if (roleSet.isEmpty()) {
            roleSet = createDefaultRoleSet(user);
        }
        return roleSet;
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
        Set<UserRole> roleSet = userRoleListToSet(userFrontDto, user);
        user.getRoles().addAll(roleSet);
        return user;
    }

    public UserDto userToDto(User user) {
        Long id = user.getId();
        String email = user.getEmail();
        String firstName = user.getFirstName();
        String lastName = user.getLastName();
        Set<UserRole> roleSet = user.getRoles();
        List<Task> taskList = user.getTaskList();
        boolean deleted = user.isDeleted();
        return new UserDto(id, email, firstName, lastName, roleSet, taskList, deleted);
    }

    public void sendPasswordResetLink(String email) {
        userRepository.findAllByEmail(email).ifPresent(user -> {
            String key = UUID.randomUUID().toString();
            user.setPasswordResetKey(key);
            try {
                mailSenderService.resetPasswordMail(email, key);
            } catch (MessagingException e) {

            }
            userRepository.save(user);
        });
    }

    public void updateUserPassword(String key, String newPassword) {
        userRepository.findByPasswordResetKey(key).ifPresent(user -> {
            user.setPassword(encodePassword(newPassword));
            user.setPasswordResetKey(null);
            userRepository.save(user);
        });
    }

    public void changePassword(long id, String currentPassword, String newPassword) {
        userRepository.findById(id).ifPresent(user -> {
            if (passwordEncoder.matches(currentPassword, user.getPassword())) {
                user.setPassword(encodePassword(newPassword));
                userRepository.save(user);
            } else {
                throw new IllegalArgumentException();
            }
        });
    }
}