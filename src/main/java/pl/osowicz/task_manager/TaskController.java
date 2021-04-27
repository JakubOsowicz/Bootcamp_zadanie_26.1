package pl.osowicz.task_manager;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class TaskController {

    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    public TaskController(UserRepository userRepository, TaskRepository taskRepository) {
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
    }

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/addTask")
    public String addTask(Model model) {
        Task task = new Task();
        List<User> users = userRepository.findAll();
        model.addAttribute("task", task);
        model.addAttribute("users", users);
        return "addTask";
    }

    @PostMapping("/addTask")
    public String addTaskToDatabase(Task task) {
        taskRepository.save(task);
        return "redirect:/";
    }

    @GetMapping("/taskList")
    public String showTasks(@RequestParam(required = false) Status status, Model model) {
        List<Task> tasks;
        if (status == null) {
            tasks = taskRepository.findAll();
        } else {
            tasks = taskRepository.findAllByAndStatusEquals(status);
        }
        model.addAttribute("tasks", tasks);
        return "taskList";
    }
}
