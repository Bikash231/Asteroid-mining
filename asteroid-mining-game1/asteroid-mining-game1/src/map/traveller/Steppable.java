package map.traveller;

/**
 * This interface is used by Game-controlled classes that step every time a round ends.
 */
public interface Steppable {
    /**
     * This method is called by the game every time all the players have stepped in a given round. Classes that
     * implement this interface should implement this method so that when it's called, exactly one operation is
     * executed, and that operation is valid for the given object.
     */
    void step();

    /**
     * The steppable is destroyed
     * Dying should be implemented in all steppable, to remove it from the list of steppable objects in Game
     */
    void die();
}
