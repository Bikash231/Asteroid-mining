package map.resource;

/**
 * Class map.asteroid.Ice
 * A type of sublimable resource (disappears if drilled in perihelion)
 */
public class Ice extends SublimableResource {

    private static final long serialVersionUID = 5260345601311354820L;

    public Ice() {
    }

    @Override
    public Resource clone() {
        return new Ice();
    }

    // needed for displaying the resource's name in AsteroidStatusView
    @Override
    public String toString() {
        return "Ice";
    }
}
