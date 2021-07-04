package pl.osowicz.task_manager.security;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping("/resetPassword")
    public String resetPassword(@RequestParam(name = "key") String key, Model model) {
        model.addAttribute("key", key);
        return "security/resetPassword";
    }

    @PostMapping("/resetPassword")
    public String resetPasswordProcess(@RequestParam(name = "key") String key,
                                       @RequestParam(name = "newPassword") String newPassword) {
        userService.updateUserPassword(key, newPassword);
        return "index";
    }

    @GetMapping("forgotPassword")
    public String forgotPassword() {
        return "security/forgotPassword";
    }

    @PostMapping("/forgotPassword")
    public String forgotPasswordProcess(@RequestParam(name = "email") String email) {
        userService.sendPasswordResetLink(email);
        return "security/mailSendSuccess";
    }

//    @GetMapping("/changePassword")
//    public String changePassword() {
//        return "security/changePassword";
//    }
//
//    @PostMapping("/changePassword")
//    public String processChangePassword(@RequestParam String currentPassword,
//                                        @RequestParam String newPassword,
//                                        @RequestParam String confirmNewPassword,
//                                        Principal principal, Model model) {
//        if (newPassword.equals(confirmNewPassword)) {
//            userService.changePassword(principal.getName(), currentPassword, newPassword);
//            return "index";
//        }
//        return "redirect:/login";
//    }
}
