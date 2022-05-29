package map.resource;

/**
 * Class map.asteroid.Iron
 * A type of normal resource (without any special behaviour)
 */
public class Iron extends Resource {

    private static final long serialVersionUID = 3888305902827503176L;

    public Iron() {
    }

    @Override
    public Resource clone() {
        return new Iron();
    }

    // needed for displaying the resource's name in AsteroidStatusView
    @Override
    public String toString() {
        return "Iron";
    }
}
