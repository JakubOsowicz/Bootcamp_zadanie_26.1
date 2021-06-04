package pl.osowicz.task_manager.user;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.osowicz.task_manager.user.dtos.UserAddDto;
import pl.osowicz.task_manager.user.dtos.UserEditDto;

import java.util.*;

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
        UserAddDto userAddDto = new UserAddDto();
        model.addAttribute("user", userAddDto);
        return "user/add";
    }

    @PostMapping("/add")
    public String addUserToDatabase(UserAddDto userAddDto) {
        List<Role> roles = userAddDto.getRoleList();
        User user = userService.addDtoToUser(userAddDto);
        userService.save(user);
        return "redirect:/";
    }

    @GetMapping("/edit")
    public String editUser(@RequestParam(name = "id") Long id, Model model) {
        User user = userService.findById(id);
        UserEditDto userEditDto = userService.userToEditDto(user);
        model.addAttribute("user", userEditDto);
        return "user/edit";
    }

    @PostMapping("/edit")
    public String saveEditedUser(UserEditDto userEditDto) {
        User user = userService.editDtoToUser(userEditDto);
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
