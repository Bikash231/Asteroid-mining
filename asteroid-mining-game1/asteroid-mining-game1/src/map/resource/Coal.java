package map.resource;

/**
 * Class map.asteroid.Coal
 * A type of normal resource (without any special behaviour)
 */
public class Coal extends Resource {

    private static final long serialVersionUID = -2088226317064543755L;

    public Coal() {
    }

    @Override
    public Resource clone() {
        return new Coal();
    }

    // needed for displaying the resource's name in AsteroidStatusView
    @Override
    public String toString() {
        return "Coal";
    }
}
