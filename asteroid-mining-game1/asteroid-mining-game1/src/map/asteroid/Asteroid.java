package map.asteroid;

import control.ActionFailedException;
import control.Game;
import map.traveller.Traveller;
import map.traveller.TeleportationGate;
import map.resource.Resource;
import view.AsteroidView;
import view.GameWindow;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Class map.asteroid.Asteroid
 */
public class Asteroid implements Serializable {

    private static final long serialVersionUID = -8591675552003339149L;
    /**
     * List of entities that are on the surface of the asteroid.
     */
    protected final ArrayList<Traveller> travellers = new ArrayList<>();
    /**
     * List of the neighbours.
     */
    protected final ArrayList<Asteroid> listOfNeighbours = new ArrayList<>();
    /**
     * The view that displays the asteroid.
     */
    protected final AsteroidView asteroidView;
    /**
     * Surface thickness.
     */
    protected int depth;
    /**
     * Gives whether the asteroid located inside the perihelion zone.
     */
    protected boolean inPerihelion;
    /**
     * Name of the asteroid.
     */
    protected String name;
    /**
     * The resource that is located in the core.
     */
    protected Resource resource;
    /**
     * The teleport gate that is attached to the asteroid.
     */
    protected TeleportationGate teleportationGate;

    public Asteroid() { // BaseAsteroid's ctor calls this
        this.asteroidView = new AsteroidView(this);
        this.asteroidView.updateView();
    }

    /**
     * Constructor for the map initialization.
     *
     * @param name             Name of the asteroid.
     * @param inPerihelion     Gives whether the asteroid located inside the perihelion zone.
     * @param depth Surface thickness.
     * @param res              Resource to be set as core of the Asteroid (null if empty).
     */
    public Asteroid(String name, boolean inPerihelion, int depth, Resource res) {
        this.name = name;
        this.inPerihelion = inPerihelion;
        this.depth = depth;
        teleportationGate = null;
        resource = res;
        if (this.resource != null) {
            this.resource.setAsteroid(this);
        }
        this.asteroidView = new AsteroidView(this);
        this.asteroidView.updateView();
    }

    /**
     * Getter of the asteroidview. Used in MapView generation.
     *
     * @return The AsteroidView object.
     */
    public AsteroidView getAsteroidView() {
        return asteroidView;
    }

    /**
     * Getter for the asteroid's name.
     *
     * @return The asteroid's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Get the value of surfaceThickness
     *
     * @return the value of surfaceThickness
     */
    public int getDepth() {
        return depth;
    }

    /**
     * Get the value of inPerihelion
     *
     * @return the value of inPerihelion
     */
    public boolean getInPerihelion() {
        return inPerihelion;
    }

    /**
     * Get the asteroid's resource
     *
     * @return The resource which the asteroid contains
     */
    public Resource getResource() {
        return resource;
    }

    /**
     * Get the TeleportGate of this asteroid
     *
     * @return The TeleportGate object, null if there is none
     */
    public TeleportationGate getTeleportGate() {
        return teleportationGate;
    }

    /**
     * Asteroid explodes. The asteroid is removed from the map and the neighbours of the asteroid
     * will be each other's neighbours.
     */
    public void explode() {
        asteroidView.explosionNotification();
        // All entities that were on the asteroid are warned that the asteroid has been exploded
        int size = travellers.size();
        for (int i = 0; i < travellers.size(); i++) {
            travellers.get(i).asteroidExploded();
            if (size > travellers.size()) {
                i = i - (size - travellers.size());
                size = travellers.size();
            }
        }

        if (teleportationGate != null)
            teleportationGate.die();

        // Modifying neighbours to preserve consistency
        for (int i = 0; i < listOfNeighbours.size(); i++) {
            listOfNeighbours.get(i).removeAsteroid(this);
            for (int j = 0; j < listOfNeighbours.size(); j++) // Connecting the neighbours to each other
                if (i != j) listOfNeighbours.get(i).addNeighbour(listOfNeighbours.get(j));
        }
        Game.getInstance().getMap().removeAsteroid(this);
        GameWindow.getMapView().updateView(this);
    }

    /**
     * The asteroid is drilled.
     */
    public void drilled() throws ActionFailedException {
        // You can't drill if the surface thickness is 0
        if (depth != 0) {
            this.depth--;
            // If the asteroid is in the perihelion zone, some resources behave different, that's why drilledInPerihelion() is called
            if (resource != null && depth == 0 && inPerihelion) {
                this.resource.drilledInPerihelion();
            }
        } else {
            throw new ActionFailedException("The surface thickness is 0, cannot drill");
        }
    }

    /**
     * The asteroid is mined.
     *
     * @return the mined asteroid (map.asteroid.Resource)
     */
    public Resource extractResource() throws ActionFailedException {
        Resource minedResource;
        // If the asteroid is not empty and the thickness is zero, the resource is mined
        if (resource != null) {
            if (depth == 0) {
                minedResource = resource;
                resource = null;
                minedResource.setAsteroid(null);
            } else {
                throw new ActionFailedException("Surface thickness is not 0, cannot mine");
            }
        } else {
            throw new ActionFailedException("There is no resource inside the core");
        }

        return minedResource;
    }

    /**
     * Gets all neighbours
     *
     * @return A map.asteroid.Neighbours object that contains all the neighbours of the asteroid
     */
    public Neighbours getListOfNeighbours() {
        // The Neighbours constructor expects an ArrayList containing the asteroids connected by a teleport gate
        ArrayList<Asteroid> teleportGateOtherSide = new ArrayList<>();
        if (teleportationGate != null && teleportationGate.getOtherSide() != null) {
            teleportGateOtherSide.add(teleportationGate.getOtherSide());
        }
        return new Neighbours(listOfNeighbours, teleportGateOtherSide);
    }

    /**
     * Gets all the entities who are on this asteroid.
     *
     * @return The entities who are staniding on this asteroid.
     */
    public ArrayList<Traveller> getTravellers() {
        return travellers;
    }

    /**
     * @return An asteroid list whose don't have teleportgate.
     */
    public ArrayList<Asteroid> getNeighboursWithoutTeleportGate() {
        return listOfNeighbours;
    }

    /**
     * Places an entity onto the surface of the asteroid
     *
     * @param traveller that will be added to the list
     */
    public void acceptTraveller(Traveller traveller) {
        this.travellers.add(traveller);
    }

    /**
     * Removes an entity from the asteroid
     *
     * @param traveller that will be removed from the list
     */
    public void removeTraveller(Traveller traveller) {
        travellers.remove(traveller);
    }

    /**
     * Adds a new neighbour to the asteroid
     *
     * @param asteroid new neighbour of the asteroid
     */
    public void addNeighbour(Asteroid asteroid) {
        if (!listOfNeighbours.contains(asteroid) && asteroid != this)
            this.listOfNeighbours.add(asteroid);
    }

    /**
     * Removes a neighbour
     *
     * @param asteroid remove this asteroid from it's neighbours
     */
    public void removeAsteroid(Asteroid asteroid) {
        this.listOfNeighbours.remove(asteroid);
    }

    /**
     * Sets the asteroid's teleport gate.
     *
     * @param teleportationGate the teleport gate that will be placed on the asteroid
     * @return success or not
     */
    public boolean setTeleportationGate(TeleportationGate teleportationGate) {
        if (teleportationGate == null) {
            this.teleportationGate = null;
            return true;
        } else if (this.teleportationGate == null) {
            Game.getInstance().addSteppable(teleportationGate);
            this.teleportationGate = teleportationGate;
            this.teleportationGate.setCurrentAsteroid(this);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Removes the teleport gate from the asteroid.
     */
    public void removeTeleportationGate() {
        this.teleportationGate = null;
    }

    /**
     * Places a resource inside the asteroid.
     *
     * @param resource this will be placed in the asteroid
     */
    public boolean placeResource(Resource resource) {
        // if the asteroid is empty and the surface thickness is zero, we can place the resource inside the asteroid
        if (this.resource == null && depth == 0) {
            this.resource = resource;
            this.resource.setAsteroid(this);
            return true;
        }
        return false;
    }

    /**
     * A sunflare hits the asteroid.
     */
    public void hitBySunflare() {
        // If the asteroid is not empty, all the entities die on its surface
        if (resource != null || depth != 0) {
            ArrayList<Traveller> entities2 = new ArrayList<>(travellers);
            for (Traveller traveller : entities2)
                traveller.die();
        }
        if (teleportationGate != null)
            teleportationGate.hitBySunStorm();
    }

    /**
     * Changes the asteroid's perihelion state.
     */
    public void changePerihelionState() {
        this.inPerihelion = !inPerihelion;
    }

    /**
     * Exposes the resource in the asteroid's core if the surface is fully drilled and the asteroid is in perihelion.
     */
    public void expose() {
        if (depth == 0 && inPerihelion && resource != null)
            resource.exposed();
    }
}
