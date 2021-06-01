package pl.osowicz.task_manager.task;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.osowicz.task_manager.user.User;
import pl.osowicz.task_manager.user.UserService;

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
        List<User> users = userService.getActiveUsers();
        model.addAttribute("task", new Task());
        model.addAttribute("users", users);
        return "/task/add";
    }

    @PostMapping("/add")
    public String addTaskToDatabase(Task task) {
        taskService.setTaskStatus(task);
        taskService.saveTask(task);
        return "redirect:/";
    }

    @GetMapping("/list")
    public String showTasks(@RequestParam(required = false) Status status, Model model) {
        List<Task> tasks = taskService.getCustomTaskList(status);
        model.addAttribute("tasks", tasks);
        model.addAttribute("status", status);
        return "/task/list";
    }

    @GetMapping("/edit")
    public String editTask(@RequestParam(name = "id") Long id, Model model, Status status) {
        Task task = taskService.findById(id);
        List<User> users = userService.getActiveUsers();
        model.addAttribute("task", task);
        model.addAttribute("users", users);
        model.addAttribute("listStatus", status);
        return "task/edit";
    }

    @PostMapping("/edit")
    public String saveEditedTask(Task task, @RequestParam(name = "status", required = false) Status listStatus) {
        taskService.setTaskStatus(task);
        taskService.saveTask(task);
        return taskService.redirectToPreviousTaskList(listStatus);
    }

    @PostMapping("/delete")
    public String deleteTaskFromDatabase(@RequestParam(name = "id") Long id, @RequestParam(name = "status", required = false) Status listStatus) {
        taskService.deleteById(id);
        return taskService.redirectToPreviousTaskList(listStatus);
    }

    @PostMapping("/done")
    public String endTask(@RequestParam(name = "id") Long id, @RequestParam(name = "status", required = false) Status listStatus) {
        Task task = taskService.findById(id);
        taskService.setTaskStatusCompleted(task);
        return taskService.redirectToPreviousTaskList(listStatus);
    }
}