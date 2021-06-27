package pl.osowicz.task_manager;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pl.osowicz.task_manager.user.User;
import pl.osowicz.task_manager.user.UserService;

import java.security.Principal;

@Controller
public class HomeController {

    private final UserService userService;

    public HomeController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String home(Principal principal, Model model) {
        if (principal != null) {
            User currentUser = userService.findByEmail(principal.getName());
            model.addAttribute("currentUser", currentUser);
        }
        return "index";
    }
}