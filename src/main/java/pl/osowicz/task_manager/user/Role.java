package pl.osowicz.task_manager.user;

public enum Role {
    ROLE_ADMIN("Administrator"),
    ROLE_USER("Użytkownik"),
    ROLE_GUEST("Gość");

    private final String plName;

    Role(String plName) {
        this.plName = plName;
    }

    public String getPlName() {
        return plName;
    }
}
