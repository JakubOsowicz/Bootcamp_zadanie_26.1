package pl.osowicz.task_manager;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    public String editUser(@RequestParam(name = "id") Long id, Model model) {
        User user = userRepository.getOne(id);
        model.addAttribute("user", user);
        return "user/editUser";
    }

    @PostMapping("/editUser")
    public String saveEditedUser(User user) {
        userRepository.save(user);
        return "redirect:/";
    }

    @RequestMapping("/deleteUser")
    public String deleteUserFromDatabase(@RequestParam(name = "id") Long id) {
        User user = userRepository.getOne(id);
        if (user.getTaskList().isEmpty()) {
            userRepository.deleteById(id);
        } else {
            return "redirect:/deleteError";
        }
        return "redirect:/";
    }
}
