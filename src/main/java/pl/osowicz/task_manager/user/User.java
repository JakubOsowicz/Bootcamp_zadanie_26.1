package pl.osowicz.task_manager.user;

import pl.osowicz.task_manager.task.Task;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;

    private String password;
    private String firstName;
    private String lastName;

    @Column(nullable = false)
    @OneToMany(mappedBy = "user")
    private Set<UserRole> roles;

    @OneToMany(mappedBy = "user")
    private List<Task> taskList;

    @Column()
    private boolean deleted = false;

    public User() {
    }

    public User(Long id, String email, String password, String firstName, String lastName, Set<UserRole> roles, List<Task> taskList, boolean deleted) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.roles = roles;
        this.taskList = taskList;
        this.deleted = deleted;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Set<UserRole> getRoles() {
        return roles;
    }

    public void setRole(Set<UserRole> roles) {
        this.roles = roles;
    }

    public List<Task> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean active) {
        this.deleted = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return deleted == user.deleted && Objects.equals(id, user.id) && Objects.equals(email, user.email) && Objects.equals(password, user.password) && Objects.equals(firstName, user.firstName) && Objects.equals(lastName, user.lastName) && Objects.equals(roles, user.roles) && Objects.equals(taskList, user.taskList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, password, firstName, lastName, roles, taskList, deleted);
    }
}
