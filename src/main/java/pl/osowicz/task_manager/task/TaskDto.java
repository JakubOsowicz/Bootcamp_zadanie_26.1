package pl.osowicz.task_manager.task;

import org.springframework.format.annotation.DateTimeFormat;
import pl.osowicz.task_manager.user.User;

import java.time.LocalDateTime;

public class TaskDto {

    private final String datePattern = "yyyy-MM-dd'T'HH:mm";

    private Long id;
    private String name;
    private String description;
    private User user;
    private Status status;

    @DateTimeFormat(pattern = datePattern)
    private LocalDateTime startDate;

    @DateTimeFormat(pattern = datePattern)
    private LocalDateTime endDate;

    @DateTimeFormat(pattern = datePattern)
    private LocalDateTime deadLine;

    public TaskDto() {
    }

    public TaskDto(Long id, String name, String description, User user, Status status, LocalDateTime startDate, LocalDateTime endDate, LocalDateTime deadLine) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.user = user;
        this.status = status;
        this.startDate = startDate;
        this.endDate = endDate;
        this.deadLine = deadLine;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public LocalDateTime getDeadLine() {
        return deadLine;
    }

    public void setDeadLine(LocalDateTime deadLine) {
        this.deadLine = deadLine;
    }
}
