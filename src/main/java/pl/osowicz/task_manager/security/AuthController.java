package pl.osowicz.task_manager.security;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.osowicz.task_manager.user.User;
import pl.osowicz.task_manager.user.UserService;
import pl.osowicz.task_manager.user.dtos.UserDto;

import java.security.Principal;

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
        model.addAttribute("user", new UserDto());
        return "security/register";
    }

    @PostMapping("/register")
    public String processRegister(UserDto userDto) {
        userService.registerNewUser(userDto);
        return "redirect:login";
    }

    @GetMapping("/resetPassword")
    public String resetPassword(@RequestParam(name = "key") String key, Model model) {
        model.addAttribute("key", key);
        return "account/resetPassword";
    }

    @PostMapping("/resetPassword")
    public String resetPasswordProcess(@RequestParam(name = "key") String key,
                                       @RequestParam(name = "newPassword") String newPassword) {
        userService.updateUserPassword(key, newPassword);
        return "index";
    }

    @GetMapping("forgotPassword")
    public String forgotPassword() {
        return "account/forgotPassword";
    }

    @PostMapping("/forgotPassword")
    public String forgotPasswordProcess(@RequestParam(name = "email") String email) {
        userService.sendPasswordResetLink(email);
        return "success/mailSendSuccess";
    }

    @GetMapping("/changePassword")
    public String changePassword(@RequestParam(name = "error", required = false) boolean error,
                                 @RequestParam(name = "oldError", required = false) boolean oldError,
                                 Principal principal, Model model) {
        User user = userService.findByEmail(principal.getName());
        String username = user.getFirstName() + " " + user.getLastName();
        long id = user.getId();
        model.addAttribute("username", username);
        model.addAttribute("id", id);
        model.addAttribute("error", error);
        model.addAttribute("oldError", oldError);
        return "account/changePassword";
    }

    @PostMapping("/changePassword")
    public String processChangePassword(@RequestParam String currentPassword,
                                        @RequestParam String newPassword,
                                        @RequestParam String confirmNewPassword,
                                        @RequestParam long id) {
        if (newPassword.equals(confirmNewPassword)) {
            try {
                userService.changePassword(id, currentPassword, newPassword);
                return "success/passwordChangeSuccess";
            } catch (IllegalArgumentException e) {
                return "redirect:/changePassword?oldError=true";
            }
        }
        return "redirect:/changePassword?error=true";
    }
}