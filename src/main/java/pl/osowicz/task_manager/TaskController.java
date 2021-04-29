package pl.osowicz.task_manager;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

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
        List<User> users = userRepository.findAll();
        Task task = new Task();
        model.addAttribute("task", task);
        model.addAttribute("users", users);
        return "addTask";
    }

    @PostMapping("/addTask")
    public String addTaskToDatabase(Task task) {
        setTaskStatus(task);
        taskRepository.save(task);
        return "redirect:/";
    }

    private void setTaskStatus(Task task) {
        if (task.getUser() == null) {
            task.setStatus(Status.NOT_ASSIGNED);
            task.setStartDate(null);
        } else if (task.getUser() != null && task.getStartDate() == null) {
            task.setStatus(Status.ASSIGNED);
        } else if (task.getUser() != null && task.getStartDate() != null) {
            task.setStatus(Status.STARTED);
        }
    }

    private void setTaskStatusCompleted(Task task) {
        task.setStatus(Status.COMPLETED);
    }

    @GetMapping("/taskList")
    public String showTasks(@RequestParam(required = false) Status status, Model model) {
        List<Task> tasks;
        if (status == null) {
            tasks = taskRepository.findAll();
        } else if (Status.COMPLETED.equals(status)) {
            tasks = taskRepository.findAllByAndStatusEquals(status);
        } else {
            tasks = taskRepository.findAllByStatusIsNot(Status.COMPLETED);
        }
        model.addAttribute("tasks", tasks);
        return "taskList";
    }

    @GetMapping("/editTask")
    public String editTask(@RequestParam(name = "id") Long id, Model model) {
        Optional<Task> task = taskRepository.findById(id);
        if (task.isPresent()) {
            List<User> users = userRepository.findAll();
            model.addAttribute("task", task.get());
            model.addAttribute("users", users);
        } else {
            return "redirect:/";
        }
        return "editTask";
    }

    @PostMapping("/editTask")
    public String saveEditedTask(Task task) {
        taskRepository.save(task);
        return "redirect:/taskList";
    }

    @RequestMapping("/deleteTask")
    public String deleteTaskFromDatabase(@RequestParam(name = "id") Long id) {
        taskRepository.deleteById(id);
        return "redirect:/";
    }
}
