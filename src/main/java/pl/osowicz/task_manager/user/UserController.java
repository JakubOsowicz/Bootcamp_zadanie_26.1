package pl.osowicz.task_manager.user;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.osowicz.task_manager.task.TaskRepository;

import java.util.List;

@Controller
public class UserController {

    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    public UserController(UserRepository userRepository, TaskRepository taskRepository) {
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
    }

    @GetMapping("/user/list")
    public String showUsers(Model model) {
        List<User> users = userRepository.findAllByDeleted(false);
        model.addAttribute("users", users);
        return "user/list";
    }

    @GetMapping("/user/add")
    public String addUser(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "user/add";
    }

    @PostMapping("/user/add")
    public String addUserToDatabase(User user) {
        userRepository.save(user);
        return "redirect:/";
    }

    @GetMapping("/user/edit")
    public String editUser(@RequestParam(name = "id") Long id, Model model) {
        User user = userRepository.getOne(id);
        model.addAttribute("user", user);
        return "user/edit";
    }

    @PostMapping("/user/edit")
    public String saveEditedUser(User user) {
        userRepository.save(user);
        return "redirect:/";
    }

    @RequestMapping("/user/delete")
    public String deleteUserFromDatabase(@RequestParam(name = "id") Long id) {
        User user = userRepository.getOne(id);
        if (user.getTaskList().isEmpty()) {
            userRepository.deleteById(id);
        } else {
            user.setDeleted(false);
            userRepository.save(user);
        }
        return "redirect:/";
    }

    @GetMapping("/user/details")
    public String showUserDetails(@RequestParam(name = "id") Long id, Model model) {
        User user = userRepository.getOne(id);
        model.addAttribute("user", user);
        return "user/details";
    }
}
