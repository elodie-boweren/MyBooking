public class NotFoundException extends RuntimeException {
    // Error message only
    public NotFoundException(String message) {
        super(message);
    }
    // Error message and cause
    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}