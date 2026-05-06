package sportbets.web.error;

public class TippValidationException extends RuntimeException {

    public TippValidationException(String message) {
        super(message);
    }
    public TippValidationException (String message, Throwable cause) {
        super (message, cause);
    }
}
