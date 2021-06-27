package pl.osowicz.task_manager.user.dtos;

import pl.osowicz.task_manager.task.Task;
import pl.osowicz.task_manager.user.UserRole;

import java.util.List;
import java.util.Set;

public class UserDto {

    private Long id;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private Set<UserRole> roles;
    private List<Task> taskList;
    private boolean deleted = false;

    public UserDto() {
    }

    public UserDto(Long id, String firstName, String lastName) {
        this(id, null, null, firstName, lastName, null, null, false);
    }

    public UserDto(Long id, String email, String firstName, String lastName, Set<UserRole> roles) {
        this(id, email, null, firstName, lastName, roles, null, false);
    }

    public UserDto(Long id, String email, String firstName, String lastName, Set<UserRole> roles, List<Task> taskList, boolean deleted) {
        this(id, email, null, firstName, lastName, roles, taskList, deleted);
    }

    public UserDto(Long id, String email, String password, String firstName, String lastName, Set<UserRole> roles, List<Task> taskList, boolean deleted) {
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

    public void setRoles(Set<UserRole> roles) {
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

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
