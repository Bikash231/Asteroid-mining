package map.resource;

/**
 * Class RadioactiveResource
 * Special resource, explodes if the asteroid is drilled in perihelion
 */
public abstract class RadioactiveResource extends Resource {

    public RadioactiveResource() {
    }

    /**
     * Called if the asteroid's core is drilled in perihelion
     * Generates a nuclear explosion
     */
    public void drilledInPerihelion() {
        asteroid.explode(); // explosion
    }
}
