package pl.osowicz.task_manager.task;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.osowicz.task_manager.user.User;
import pl.osowicz.task_manager.user.UserService;
import pl.osowicz.task_manager.user.dtos.UserDto;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/task")
public class TaskController {

    private final TaskService taskService;
    private final UserService userService;

    public TaskController(TaskService taskService, UserService userService) {
        this.taskService = taskService;
        this.userService = userService;
    }

    @GetMapping("/add")
    public String addTask(Model model) {
        List<UserDto> usersDto = userService.getActiveUsersDto();
        model.addAttribute("task", new TaskDto());
        model.addAttribute("users", usersDto);
        return "/task/add";
    }

    @PostMapping("/add")
    public String addTaskToDatabase(TaskDto taskDto) {
        Task task = taskService.dtoToTask(taskDto);
        taskService.setTaskStatus(task);
        taskService.saveTask(task);
        return "redirect:/";
    }

    @GetMapping("/list")
    public String showTasks(@RequestParam(required = false) Status status, Model model) {
        List<TaskDto> tasksDto = taskService.findAllByStatusDto(status);
        model.addAttribute("tasks", tasksDto);
        model.addAttribute("status", status);
        return "/task/list";
    }

    @GetMapping("/edit")
    public String editTask(@RequestParam(name = "id") Long id, Model model, Status status) {
        Task task = taskService.findById(id);
        TaskDto taskDto = taskService.taskToDto(task);
        List<UserDto> usersDto = userService.getActiveUsersDto();
        model.addAttribute("task", taskDto);
        model.addAttribute("users", usersDto);
        model.addAttribute("listStatus", status);
        return "task/edit";
    }

    @PostMapping("/edit")
    public String saveEditedTask(TaskDto taskDto, @RequestParam(name = "status", required = false) Status listStatus) {
        Task task = taskService.dtoToTask(taskDto);
        taskService.setTaskStatus(task);
        taskService.saveTask(task);
        return taskService.redirectToPreviousTaskList(listStatus);
    }

    @PostMapping("/delete")
    public String deleteTaskFromDatabase(@RequestParam(name = "id") Long id,
                                         @RequestParam(name = "status", required = false) Status listStatus) {
        taskService.deleteById(id);
        return taskService.redirectToPreviousTaskList(listStatus);
    }

    @PostMapping("/done")
    public String endTask(@RequestParam(name = "id") Long id,
                          @RequestParam(name = "status", required = false) Status listStatus) {
        Task task = taskService.findById(id);
        taskService.setTaskStatusCompleted(task);
        return taskService.redirectToPreviousTaskList(listStatus);
    }

    @PostMapping("/take")
    public String takeTask(Principal principal, @RequestParam(name = "id") Long id,
                           @RequestParam(name = "status", required = false) Status listStatus) {
        User currentUser = userService.findByEmail(principal.getName());
        taskService.assignTaskToUser(currentUser, id);
        return taskService.redirectToPreviousTaskList(listStatus);
    }

    @RequestMapping("/myTasks")
    public String showUserStartedTasks(Principal principal, Model model) {
        User user = userService.findByEmail(principal.getName());
        List<TaskDto> tasksDto = taskService.getUserNotCompletedTasksDto(user);
        model.addAttribute("tasks", tasksDto);
        return "task/list";
    }

    @PostMapping("/start")
    public String startTask(@RequestParam(name = "id") Long id, @RequestParam(name = "status", required = false) Status listStatus,
                            @RequestParam(name = "myTasks", required = false) String myTasks) {
        taskService.setTaskStatusStarted(taskService.findById(id));
        if (myTasks != null) {
            return "redirect:myTasks";
        }
        return taskService.redirectToPreviousTaskList(listStatus);
    }
}