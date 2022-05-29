package map.traveller;

import control.Game;
import map.asteroid.Asteroid;
import map.resource.*;

import java.util.ArrayList;

/**
 * Class TeleportGate
 */
public class TeleportationGate implements Steppable {

    /**
     * The bill to build a teleportgate(pair).
     */
    private final static BluePrintResources billToBuild;

    static { // initialize the billToBuild variable
        billToBuild = new BluePrintResources();
        billToBuild.addResources(new Iron());
        billToBuild.addResources(new Iron());
        billToBuild.addResources(new Ice());
        billToBuild.addResources(new Uranium());
    }

    /**
     * The asteroid, where the teleportgate placed on.
     */
    private Asteroid currentAsteroid;
    /**
     * The pair of this teleportgate.
     */
    private TeleportationGate otherGate;

    /**
     * The settler whose cargo this teleport is in.
     */
    private Settler settler;

    /**
     * Determines that the teleportgate has been hitted by sunflare.
     */
    private boolean crazy;

    /**
     * Constructor of the TeleportGate, which initialize the resources to build a gate(pair).
     */
    private TeleportationGate() {
        crazy = false;
    }

    /**
     * Creates a teleportgate(pair) if there is enough resources to build.
     *
     * @param ownedResources The resources of settler, whereof the gate can be built.
     * @return with the gate-pair.
     */
    public static ArrayList<TeleportationGate> create(ArrayList<Resource> ownedResources) {
        boolean hasResourcesToBuildTeleport = billToBuild.use(ownedResources);
        if (hasResourcesToBuildTeleport) {
            ArrayList<TeleportationGate> gates = new ArrayList<>();
            TeleportationGate t1 = new TeleportationGate();
            TeleportationGate t2 = new TeleportationGate();
            t1.otherGate = t2;
            t2.otherGate = t1;
            gates.add(t1);
            gates.add(t2);
            return gates;
        }
        return null;
    }

    /**
     * Getter of the current asteroid, not used in the test.
     *
     * @return currentAsteroid.
     */
    private Asteroid getCurrentAsteroid() {
        return currentAsteroid;
    }

    /**
     * Sets the current asteroid, not used in the test.
     */
    public void setCurrentAsteroid(Asteroid asteroid) {
        currentAsteroid = asteroid;
    }

    /**
     * Returns an asteroid, where the other gate is placed on.
     *
     * @return The asteroid, where the other gate is placed on.
     */
    public Asteroid getOtherSide() {
        return otherGate.getCurrentAsteroid();
    }

    /**
     * Reference to the settler whose cargo this teleport is in.
     *
     * @param settler Value to set.
     */
    public void setSettler(Settler settler) {
        this.settler = settler;
    }

    public void hitBySunStorm() {
        crazy = true;
    }

    /**
     * At the finish of the round the teleportgate move to anoteher asteroid (which hasn't got teleportgate),
     * if the telport gate has been hitted by sunflare before.
     */
    private void move() {
        ArrayList<Asteroid> neighbours = currentAsteroid.getNeighboursWithoutTeleportGate();
        currentAsteroid.removeTeleportationGate();
        neighbours.get(neighbours.size() - 1).setTeleportationGate(this);
    }

    /**
     * Overrides the Steppable interface's step method.
     */
    @Override
    public void step() {
        if (crazy) {
            move();
        }
    }

    /**
     * Handles the death of a teleportgate pair. A teleportgate is either
     * belongs to an asteroid or a settler, but not both. After removing it from them
     * a teleportgate needs to call its pair's die() method.
     * <p>
     * To ensure the avoidance of a circular method call, the die() method is only called
     * when the gate still belongs to an asteroid or a settler.
     */
    @Override
    public void die() {
        if (currentAsteroid != null) {
            currentAsteroid.removeTeleportationGate();
        }
        if (settler != null) {
            settler.removeTeleportationGate(this);
        }
        otherGate.die();
        Game.getInstance().removeSteppable(this);
    }
}
