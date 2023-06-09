package Collections;


public class Login {
    private int loginId;
    private int userId;
    private String username;
    private String password;

    public Login(int loginId, int userId, String username, String password) {
        this.loginId = loginId;
        this.userId = userId;
        this.username = username;
        this.password = password;
    }

    public int getLoginId() {
        return loginId;
    }

    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

}

