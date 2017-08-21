/**
 * An async service that can be used to receive messages from a communication interface (e.g. message queue)
 */
public interface InputService {
    /**
     * Start up by supplying a callback object
     */
    void initialize(InputListener listener) throws CommunicationException;


    /**
     * Close all connections to this service
     */
    void shutdown() throws CommunicationException;
}
