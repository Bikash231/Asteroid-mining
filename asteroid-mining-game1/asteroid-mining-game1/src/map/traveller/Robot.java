package map.traveller;

import control.ActionFailedException;
import control.Game;
import map.asteroid.Asteroid;
import map.resource.*;

import java.util.ArrayList;
import java.util.Random;

/**
 * Class map.entity.Robot
 */
public class Robot extends Traveller implements Steppable {

    private static final long serialVersionUID = -4871940628772599854L;
    /**
     * The resources needed to build a Robot
     */
    private static final BluePrintResources collectedResources;
    /**
     * ID to make names unique for robots.
     */
    private static int nameID = 0;

    // Initializes the list of Resources needed to build a Robot.
    static {
        collectedResources = new BluePrintResources();
        collectedResources.addResources(new Iron());
        collectedResources.addResources(new Coal());
        collectedResources.addResources(new Uranium());
    }

    /**
     * Constructor. Creates a Robot object.
     */
    private Robot(String name, Asteroid asteroid) {
        super(name);
        this.asteroid = asteroid;
        this.travel(asteroid);
    }

    /**
     * Creates a Robot object and places it on the Asteroid passed as a parameter. If the caller hasn't got enough
     * resources, nothing happens.
     *
     * @param currentAsteroid The Asteroid object on which the caller Entity is on.
     * @param ownedResources  A list of {@code Resource}s the caller has.
     */
    public static void create(Asteroid currentAsteroid, ArrayList<Resource> ownedResources) throws ActionFailedException {
        boolean hasResourcesToBuildRobot = collectedResources.use(ownedResources);
        if (hasResourcesToBuildRobot) {
            Robot robot = new Robot("Robot_" + nameID, currentAsteroid);
            nameID++;
            Game.getInstance().addSteppable(robot);
        } else {
            throw new ActionFailedException("You don't have enough resources to build a robot.");
        }
    }

    /**
     * This method is called when the asteroid on which the robot is on explodes. If this happens, the robot will
     * randomly select a neighbouring asteroid and move onto that.
     */
    public void asteroidExploded() {
        ArrayList<Asteroid> neighbours = this.asteroid.getListOfNeighbours().getAsteroidNeighbours();
        Random rnd = new Random();
        travel(neighbours.get(rnd.nextInt(neighbours.size())));
    }

    /**
     * Every time a round ends (all the Settlers have stepped), every Robot steps. This method is a basic implementation
     * of a Robot object deciding what to do.
     */
    public void step() {
        Random rnd = new Random();
        int choice = rnd.nextInt(2); // generated number will be 0 or 1
        ArrayList<Asteroid> neighbours = this.asteroid.getListOfNeighbours().getAsteroidNeighbours();
        if (choice == 0 && !(neighbours.size() < 1)) {
            travel(neighbours.get(rnd.nextInt(neighbours.size())));
        } else {
            try {
                drill();
            } catch (ActionFailedException e) {
                travel(neighbours.get(rnd.nextInt(neighbours.size())));
            }
        }
    }

    /**
     * Robot dies.
     */
    @Override
    public void die() {
        super.die();
        Game.getInstance().removeSteppable(this);
    }
}
