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

    List<Task> findAllByStatus(Status status) {
        return taskRepository.findAllByAndStatusEquals(status);
    }

    Task findById(Long id) {
        Optional<Task> task = taskRepository.findById(id);
        return task.orElse(null);
    }

    List<Task> getCustomTaskList(Status status) {
        List<Task> tasks;
        if (status == null) {
            tasks = findAll();
        } else if (Status.COMPLETED.equals(status)) {
            tasks = findAllByStatus(status);
        } else {
            tasks = findAllNotCompletedTasks();
        }
        return tasks;
    }

    void saveTask(Task task) {
        taskRepository.save(task);
    }

    void setTaskStatusCompleted(Task task) {
        task.setStatus(Status.COMPLETED);
        task.setEndDate(LocalDateTime.now());
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

    List<Task> findAllNotCompletedTasks() {
        return taskRepository.findAllByStatusIsNotOrderByDeadLine(Status.COMPLETED);
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

    public List<TaskDto> getListOfTaskDtos(Status status) {
        List<Task> tasks = getCustomTaskList(status);
        return tasks
                .stream()
                .map(this::taskToDto)
                .collect(Collectors.toList());
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
