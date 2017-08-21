import domain.entity.Offence;

/**
 * Callback interface for incoming messages
 */
public interface InputListener {
    /**
     *
     * Called by the InputListener when a new message arrives
     * @param
     */
    void onReceive(Offence offence);
}
