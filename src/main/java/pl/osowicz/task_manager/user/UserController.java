package pl.osowicz.task_manager.user;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.osowicz.task_manager.user.dtos.UserDto;
import pl.osowicz.task_manager.user.dtos.UserFrontDto;

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
        List<UserDto> usersDto = userService.getActiveUsersFullDto();
        model.addAttribute("users", usersDto);
        return "user/list";
    }

    @GetMapping("/add")
    public String addUser(Model model) {
        UserFrontDto userFrontDto = new UserFrontDto();
        model.addAttribute("user", userFrontDto);
        return "user/add";
    }

    @PostMapping("/add")
    public String addUserToDatabase(UserFrontDto userFrontDto) {
        User user = userService.addFrontDtoToUser(userFrontDto);
        userService.save(user);
        userService.sendPasswordResetLink(user.getEmail());
        return "redirect:/";
    }

    @GetMapping("/edit")
    public String editUser(@RequestParam(name = "id") Long id, Model model) {
        User user = userService.findById(id);
        UserFrontDto userFrontDto = userService.userToFrontDto(user);
        model.addAttribute("user", userFrontDto);
        return "user/edit";
    }

    @PostMapping("/edit")
    public String saveEditedUser(UserFrontDto userFrontDto) {
        User user = userService.frontDtoToUser(userFrontDto);
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
        UserDto userDto = userService.userToDto(user);
        model.addAttribute("user", userDto);
        return "user/details";
    }
}
