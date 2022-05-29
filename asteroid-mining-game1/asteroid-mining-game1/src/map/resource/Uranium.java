package map.resource;

/**
 * Class map.asteroid.Uranium
 * A type of radioactive resource (explodes if drilled in perihelion)
 */
public class Uranium extends RadioactiveResource {

    private static final long serialVersionUID = 4948576870070784803L;
    private final static int exposedThreshold = 3;
    private int exposedCount = 0;

    public Uranium() {
    }

    @Override
    public void drilledInPerihelion() {
        exposed();
    }

    /**
     * Increments the exposition counter
     * If it reaches a predetermined threshold, the asteroid explodes
     */
    @Override
    public void exposed() {
        exposedCount++;
        if (exposedCount >= exposedThreshold)
            asteroid.explode();
    }

    @Override
    public Resource clone() {
        return new Uranium();
    }

    // needed for displaying the resource's name in AsteroidStatusView
    @Override
    public String toString() {
        return "Uranium";
    }
}
