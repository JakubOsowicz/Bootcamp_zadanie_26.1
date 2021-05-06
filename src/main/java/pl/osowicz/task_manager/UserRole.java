package pl.osowicz.task_manager;

public enum UserRole {
    ADMIN("Administrator"),
    USER("Użytkownik"),
    GUEST("Gość");

    private String plName;

    UserRole(String plName) {
        this.plName = plName;
    }

    public String getPlName() {
        return plName;
    }
}
