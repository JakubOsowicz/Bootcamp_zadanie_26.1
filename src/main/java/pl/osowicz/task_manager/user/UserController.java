package pl.osowicz.task_manager.user;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/list")
    public String showUsers(Model model) {
        List<User> users = userService.getActiveUsers();
        model.addAttribute("users", users);
        return "user/list";
    }

    @GetMapping("/add")
    public String addUser(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "user/add";
    }

    @PostMapping("/add")
    public String addUserToDatabase(User user) {
        userService.save(user);
        return "redirect:/";
    }

    @GetMapping("/edit")
    public String editUser(@RequestParam(name = "id") Long id, Model model) {
        User user = userService.findById(id);
        model.addAttribute("user", user);
        return "user/edit";
    }

    @PostMapping("/edit")
    public String saveEditedUser(User user) {
        userService.save(user);
        return "redirect:/";
    }

    @PostMapping("/delete")
    public String deleteUserFromDatabase(@RequestParam(name = "id") Long id) {
        User user = userService.findById(id);
        userService.safeDelete(id, user);
        return "redirect:/";
    }

    @GetMapping("/details")
    public String showUserDetails(@RequestParam(name = "id") Long id, Model model) {
        User user = userService.findById(id);
        model.addAttribute("user", user);
        return "user/details";
    }
}
