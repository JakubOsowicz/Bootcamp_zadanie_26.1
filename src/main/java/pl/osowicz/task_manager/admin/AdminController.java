package pl.osowicz.task_manager.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.osowicz.task_manager.task.TaskDto;
import pl.osowicz.task_manager.task.TaskService;
import pl.osowicz.task_manager.user.User;
import pl.osowicz.task_manager.user.UserService;
import pl.osowicz.task_manager.user.dtos.UserDto;

import java.util.List;

@Controller
public class AdminController {

    private final UserService userService;
    private final TaskService taskService;

    public AdminController(UserService userService, TaskService taskService) {
        this.userService = userService;
        this.taskService = taskService;
    }

    @GetMapping("/assignTask")
    public String assignTask(Model model) {
        List<UserDto> usersDto = userService.getActiveUsersDto();
        List<TaskDto> tasksDto = taskService.getUnassignedTasksDto();
        if (tasksDto.isEmpty()) {
            return "redirect:/";
        }
        model.addAttribute("users", usersDto);
        model.addAttribute("tasks", tasksDto);
        return "admin/assignTask";
    }

    @PostMapping("/assignTask")
    public String assingTaskToUser(@RequestParam(name = "userId") Long userId,
                                   @RequestParam(name = "taskId") Long taskId) {
        User user = userService.findById(userId);
        taskService.assignTaskToUser(user, taskId);
        return "redirect:assignTask";
    }
}
