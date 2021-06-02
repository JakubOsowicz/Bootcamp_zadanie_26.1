package pl.osowicz.task_manager.security;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import pl.osowicz.task_manager.user.User;

@Controller
public class AuthController {

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("user", new User());
        return "security/login";
    }

    @PostMapping("/login")
    public String processLogin() {
        return "index";
    }
}
