/**
 * Wrapper for anything that goes wrong during communication with external systems
 * This can include both network failures and format conversion errors
 */
public class CommunicationException extends RuntimeException {

    public CommunicationException(String message, Exception cause) {
        super(message, cause);
    }
}
