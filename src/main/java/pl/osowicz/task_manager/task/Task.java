package pl.osowicz.task_manager.task;

import org.springframework.format.annotation.DateTimeFormat;
import pl.osowicz.task_manager.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class Task {

    private final String datePattern = "yyyy-MM-dd'T'HH:mm";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @ManyToOne
    private User user;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @DateTimeFormat(pattern = datePattern)
    private LocalDateTime startDate;

    @DateTimeFormat(pattern = datePattern)
    private LocalDateTime endDate;

    @Column(nullable = false)
    @DateTimeFormat(pattern = datePattern)
    private LocalDateTime deadLine;

    public Task() {
    }

    public Task(Long id, String name, String description, User user, Status status, LocalDateTime startDate, LocalDateTime endDate, LocalDateTime deadLine) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Task task = (Task) o;
        return Objects.equals(id, task.id) && Objects.equals(name, task.name) && Objects.equals(description, task.description) && Objects.equals(user, task.user) && status == task.status && Objects.equals(startDate, task.startDate) && Objects.equals(endDate, task.endDate) && Objects.equals(deadLine, task.deadLine);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, user, status, startDate, endDate, deadLine);
    }
}
