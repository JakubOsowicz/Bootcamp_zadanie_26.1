package pl.osowicz.task_manager.security;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import pl.osowicz.task_manager.user.User;
import pl.osowicz.task_manager.user.UserService;
import pl.osowicz.task_manager.user.dtos.UserRegistrationDto;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("user", new User());
        return "security/login";
    }

    @PostMapping("/login")
    public String processLogin() {
        return "index";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new UserRegistrationDto());
        return "security/register";
    }

    @PostMapping("/register")
    public String processRegister(UserRegistrationDto userDto) {
        User user = userService.registrationDtoToUser(userDto);
        userService.save(user);

        return "redirect:login";
    }
}
