package map.resource;

import control.ActionFailedException;
import map.asteroid.Asteroid;

import java.io.Serializable;

/**
 * Class map.asteroid.Resource
 * Represents a resource object, which is used to build things
 */
public abstract class Resource implements Serializable {

    private static final long serialVersionUID = 8605724809759453519L;
    protected Asteroid asteroid;

    /**
     * Set the asteroid field of the resource
     * This represents the asteroid to which this resource is attached
     */
    public void setAsteroid(Asteroid asteroid) {
        this.asteroid = asteroid;
    }

    /**
     * Called when the asteroid with this resource in its core is drilled in perihelion
     * Default implementation
     */
    public void drilledInPerihelion() throws ActionFailedException {
    }

    /**
     * Returns whether the parameter is the same type of resource as this resource
     *
     * @param res The other resource object
     * @return True, if the 2 resources are the same type
     */
    public boolean isCompatibleWith(Resource res) {
        return res.getClass().equals(this.getClass()); // check
    }

    public void exposed() {
    }

    /**
     * Creates a deep copy of the resource (type)
     * It just makes a new object of the same type of resource, no data is copied (eg. exposedCount)
     *
     * @return Copy of the resource
     */
    abstract public Resource clone();
}
