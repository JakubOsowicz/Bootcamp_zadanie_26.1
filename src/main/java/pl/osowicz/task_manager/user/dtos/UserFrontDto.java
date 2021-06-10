package pl.osowicz.task_manager.user.dtos;

import pl.osowicz.task_manager.task.Task;
import pl.osowicz.task_manager.user.Role;
import pl.osowicz.task_manager.user.UserRole;

import java.util.List;
import java.util.Set;

public class UserFrontDto {

    private Long id;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private List<Role> roleList;

    public UserFrontDto() {
    }

    public UserFrontDto(Long id, String email, String firstName, String lastName, List<Role> roleList) {
        this(id, email, null, firstName, lastName, roleList);
    }

    public UserFrontDto(Long id, String email, String password, String firstName, String lastName, List<Role> roleList) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.roleList = roleList;
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

    public List<Role> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<Role> roleList) {
        this.roleList = roleList;
    }
}
