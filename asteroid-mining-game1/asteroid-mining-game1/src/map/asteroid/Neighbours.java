package map.asteroid;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Class map.asteroid.Neighbours
 */
public class Neighbours implements Serializable {

    private static final long serialVersionUID = 4109286614855861924L;
    /**
     * Neighbours connected by default.
     */
    private final ArrayList<Asteroid> asteroidNeighbours;
    /**
     * Neighbours connected by teleport gate.
     */
    private final ArrayList<Asteroid> teleportGateNeighbours;

    public Neighbours(ArrayList<Asteroid> asteroidNeighbours, ArrayList<Asteroid> teleportGateNeighbours) {
        this.asteroidNeighbours = asteroidNeighbours;
        this.teleportGateNeighbours = teleportGateNeighbours;
    }

    /**
     * Get the value of asteroidNeighbours.
     *
     * @return The value of asteroidNeighbours.
     */
    public ArrayList<Asteroid> getAsteroidNeighbours() {
        return asteroidNeighbours;
    }

    /**
     * Get the value of teleportGateNeighbours.
     *
     * @return The value of teleportGateNeighbours.
     */
    public ArrayList<Asteroid> getTeleportGateNeighbours() {
        return teleportGateNeighbours;
    }
}
