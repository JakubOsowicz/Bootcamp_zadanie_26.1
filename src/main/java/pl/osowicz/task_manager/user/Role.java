package pl.osowicz.task_manager.user;

public enum Role {
    ROLE_UNCONFIRMED("Niepotwierdzony"),
    ROLE_USER("Użytkownik"),
    ROLE_SUPPORT("Wsparcie"),
    ROLE_ADMIN("Administrator");

    private final String plName;

    Role(String plName) {
        this.plName = plName;
    }

    public String getPlName() {
        return plName;
    }
}
