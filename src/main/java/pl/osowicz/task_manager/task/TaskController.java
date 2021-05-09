package pl.osowicz.task_manager.task;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.osowicz.task_manager.user.User;
import pl.osowicz.task_manager.user.UserRepository;

import java.time.LocalDateTime;
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

    @GetMapping("/task/add")
    public String addTask(Model model) {
        List<User> users = userRepository.findAllByActive(true);
        Task task = new Task();
        model.addAttribute("task", task);
        model.addAttribute("users", users);
        return "/task/add";
    }

    @PostMapping("/task/add")
    public String addTaskToDatabase(Task task) {
        setTaskStatus(task);
        taskRepository.save(task);
        return "redirect:/";
    }

    private void setTaskStatus(Task task) {
        if (task.getUser() == null) {
            task.setStatus(Status.NOT_ASSIGNED);
            task.setStartDate(null);
        } else {
            if (task.getStartDate() == null) {
                task.setStatus(Status.ASSIGNED);
            } else {
                task.setStatus(Status.STARTED);
            }
        }
    }

    private void setTaskStatusCompleted(Task task) {
        task.setStatus(Status.COMPLETED);
        task.setEndDate(LocalDateTime.now());
        taskRepository.save(task);
    }

    @GetMapping("/task/list")
    public String showTasks(@RequestParam(required = false) Status status, Model model) {
        List<Task> tasks;
        if (status == null) {
            tasks = taskRepository.findAll();
        } else if (Status.COMPLETED.equals(status)) {
            tasks = taskRepository.findAllByAndStatusEquals(status);
        } else {
            tasks = taskRepository.findAllByStatusIsNotOrderByDeadLine(Status.COMPLETED);
        }
        model.addAttribute("tasks", tasks);
        model.addAttribute("status", status);
        return "/task/list";
    }

    @GetMapping("/task/edit")
    public String editTask(@RequestParam(name = "id") Long id, Model model, Status status) {
        Optional<Task> task = taskRepository.findById(id);
        if (task.isPresent()) {
            List<User> users = userRepository.findAllByActive(true);
            model.addAttribute("task", task.get());
            model.addAttribute("users", users);
            model.addAttribute("listStatus", status);
        } else {
            return "redirect:/";
        }
        return "task/edit";
    }

    @PostMapping("/task/edit")
    public String saveEditedTask(Task task, @RequestParam(name = "status", required = false) Status listStatus) {
        setTaskStatus(task);
        taskRepository.save(task);
        return redirectToPreviousTaskList(listStatus);
    }

    @DeleteMapping("/task/delete")
    public String deleteTaskFromDatabase(@RequestParam(name = "id") Long id, @RequestParam(name = "status", required = false) Status listStatus) {
        taskRepository.deleteById(id);
        return redirectToPreviousTaskList(listStatus);
    }

    @RequestMapping("/task/done")
    public String endTask(@RequestParam(name = "id") Long id, @RequestParam(name = "status", required = false) Status listStatus) {
        Optional<Task> task = taskRepository.findById(id);
        task.ifPresent(this::setTaskStatusCompleted);
        return redirectToPreviousTaskList(listStatus);
    }

    private String redirectToPreviousTaskList(@RequestParam(name = "status", required = false) Status listStatus) {
        if (listStatus == null) {
            return "redirect:/task/list?status=";
        } else {
            return "redirect:/task/list?status=" + listStatus;
        }
    }
}