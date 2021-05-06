package pl.osowicz.task_manager;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class UserController {

    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    public UserController(UserRepository userRepository, TaskRepository taskRepository) {
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
    }

    @GetMapping("/userList")
    public String showUsers(Model model) {
        List<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "user/userList";
    }

    @GetMapping("/addUser")
    public String addUser(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "user/addUser";
    }

    @PostMapping("/addUser")
    public String addUserToDatabase(User user) {
        userRepository.save(user);
        return "redirect:/";
    }

    @GetMapping("/editUser")
    public String editUser() {

        return "editUser";
    }

    @PostMapping("/editUser")
    public String saveEditedUser() {

        return "redirect:/";
    }

    @RequestMapping("/deleteUser")
    public String deleteUserFromDatabase() {

        return "redirect:/";
    }
}
