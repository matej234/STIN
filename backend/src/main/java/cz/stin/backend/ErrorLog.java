package cz.stin.backend;

public class ErrorLog {

    public String timestamp;
    public String type;
    public String message;

    public ErrorLog() {
    }

    public ErrorLog(
            String timestamp,
            String type,
            String message
    ) {
        this.timestamp = timestamp;
        this.type = type;
        this.message = message;
    }
}