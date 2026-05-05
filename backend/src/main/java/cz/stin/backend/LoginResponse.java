package cz.stin.backend;

public class LoginResponse {
    public String status;
    public String username;

    public LoginResponse(String status, String username) {
        this.status = status;
        this.username = username;
    }
}