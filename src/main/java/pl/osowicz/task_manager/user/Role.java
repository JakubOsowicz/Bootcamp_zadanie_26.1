package pl.osowicz.task_manager.user;

public enum Role {
    ROLE_ADMIN("Administrator"),
    ROLE_USER("Użytkownik"),
    ROLE_UNCONFIRMED("Niepotwierdzony"),
    ROLE_SUPPORT("Wsparcie");

    private final String plName;

    Role(String plName) {
        this.plName = plName;
    }

    public String getPlName() {
        return plName;
    }
}
