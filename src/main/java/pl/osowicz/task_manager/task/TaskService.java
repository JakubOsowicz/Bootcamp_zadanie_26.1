package pl.osowicz.task_manager.task;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
}
