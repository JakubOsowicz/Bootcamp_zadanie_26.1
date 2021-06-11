package pl.osowicz.task_manager.task;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import pl.osowicz.task_manager.user.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    List<Task> findAll() {
        return taskRepository.findAll();
    }

    public List<Task> findAllByStatus(Status status) {
        return taskRepository.findAllByAndStatusEquals(status);
    }

    Task findById(Long id) {
        Optional<Task> task = taskRepository.findById(id);
        return task.orElse(null);
    }

    public List<TaskDto> findAllByStatusDto(Status status) {
        List<Task> tasks;
        if (status == null) {
            tasks = findAll();
        } else if (Status.COMPLETED.equals(status)) {
            tasks = findAllByStatus(status);
        } else {
            tasks = findAllNotCompletedTasks();
        }
        return tasks
                .stream()
                .map(this::taskToDto)
                .collect(Collectors.toList());
    }

    public List<TaskDto> getUnassignedTasksDto() {
        return findAllByStatus(Status.NOT_ASSIGNED)
                .stream()
                .map(task -> new TaskDto(task.getId(), task.getName())).collect(Collectors.toList());
    }

    void saveTask(Task task) {
        taskRepository.save(task);
    }

    void setTaskStatusCompleted(Task task) {
        task.setStatus(Status.COMPLETED);
        task.setEndDate(LocalDateTime.now());
        saveTask(task);
    }

    void setTaskStatusStarted(Task task) {
        task.setStatus(Status.STARTED);
        task.setStartDate(LocalDateTime.now());
        saveTask(task);
    }

    void setTaskStatus(Task task) {
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

    public List<Task> findAllNotCompletedTasks() {
        return taskRepository.findAllByStatusIsNotOrderByDeadLine(Status.COMPLETED);
    }

    public List<TaskDto> getUserNotCompletedTasksDto(User user) {
        return user.getTaskList().stream()
                .filter(task -> task.getStatus() != Status.COMPLETED)
                .map(this::taskToDto)
                .collect(Collectors.toList());
    }

    void deleteById(Long id) {
        taskRepository.deleteById(id);
    }

    String redirectToPreviousTaskList(@RequestParam(name = "status", required = false) Status listStatus) {
        if (listStatus == null) {
            return "redirect:/task/list?status=";
        } else {
            return "redirect:/task/list?status=" + listStatus;
        }
    }

    public void assignTaskToUser(User currentUser, Long id) {
        Task task = findById(id);
        task.setUser(currentUser);
        setTaskStatus(task);
        saveTask(task);
    }

    public TaskDto taskToDto(Task task) {
        Long id = task.getId();
        String name = task.getName();
        String description = task.getDescription();
        User user = task.getUser();
        Status status = task.getStatus();
        LocalDateTime startDate = task.getStartDate();
        LocalDateTime endDate = task.getEndDate();
        LocalDateTime deadLine = task.getDeadLine();
        return new TaskDto(id, name, description, user, status, startDate, endDate, deadLine);
    }

    public Task dtoToTask(TaskDto taskDto) {
        Long id = taskDto.getId();
        String name = taskDto.getName();
        String description = taskDto.getDescription();
        User user = taskDto.getUser();
        Status status = taskDto.getStatus();
        LocalDateTime startDate = taskDto.getStartDate();
        LocalDateTime endDate = taskDto.getEndDate();
        LocalDateTime deadLine = taskDto.getDeadLine();
        return new Task(id, name, description, user, status, startDate, endDate, deadLine);
    }
}
