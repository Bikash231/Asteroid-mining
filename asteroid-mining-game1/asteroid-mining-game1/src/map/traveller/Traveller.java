package map.traveller;

import control.ActionFailedException;
import map.asteroid.Asteroid;
import map.resource.Resource;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Class map.entity.Entity
 */
abstract public class Traveller implements Serializable {
    private static final long serialVersionUID = 2523005641127157231L;
    /**
     * Entity's name.
     */
    protected String name;
    /**
     * Asteroid.
     */
    protected Asteroid asteroid;

    /**
     * Constructor of Entity.
     */
    public Traveller(String name) {
        this.name = name;
    }

    /**
     * Getter for the entity's asteroid.
     * @return The asteroid the settler is currently standing on.
     */
    public Asteroid getAsteroid() {
        return asteroid;
    }

    /**
     * Get the value of name
     *
     * @return the value of name
     */
    public String getName() {
        return name;
    }

    /**
     * Moves the entity from current asteroid to the asteroid given as parameter.
     *
     * @param whereTo Given asteroid.
     */
    public void travel(Asteroid whereTo) {
        if (asteroid != null) {
            asteroid.removeTraveller(this);
        }
        whereTo.acceptTraveller(this);
        asteroid = whereTo;
    }

    /**
     * Entity tries to drill.
     */
    public void drill() throws ActionFailedException {
        asteroid.drilled();
    }

    /**
     * Entity dies.
     */
    public void die() {
        asteroid.removeTraveller(this);
    }

    /**
     * Handles the event of asteroid explosion on the exposed entity.
     */
    public void asteroidExploded() {
        die();
    }

    /**
     * Returns a list of resources in carry capable entities. Returns null by default.
     *
     * @return null.
     */
    public ArrayList<Resource> getResources() {
        return null;
    }
}
