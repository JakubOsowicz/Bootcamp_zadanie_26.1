package pl.osowicz.task_manager.security;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import pl.osowicz.task_manager.user.User;
import pl.osowicz.task_manager.user.UserService;
import pl.osowicz.task_manager.user.dtos.UserDto;
import pl.osowicz.task_manager.user.dtos.UserFrontDto;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("user", new UserDto());
        return "security/login";
    }

    @PostMapping("/login")
    public String processLogin() {
        return "index";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new UserFrontDto());
        return "security/register";
    }

    @PostMapping("/register")
    public String processRegister(UserFrontDto userFrontDto) {
        User user = userService.registerDtoToUser(userFrontDto);
        userService.save(user);
        return "redirect:login";
    }
}
