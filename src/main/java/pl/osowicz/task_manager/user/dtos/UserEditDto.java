package pl.osowicz.task_manager.user.dtos;

import pl.osowicz.task_manager.user.Role;

import java.util.List;

public class UserEditDto {

    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private List<Role> roleList;

    public UserEditDto() {
    }

    public UserEditDto(Long id, String email, String firstName, String lastName, List<Role> roleList) {
        this.id = id;
        this.email = email;
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
