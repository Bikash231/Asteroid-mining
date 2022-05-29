package map.asteroid;

import control.Game;
import map.traveller.Traveller;
import map.resource.*;

import java.util.ArrayList;

/**
 * Class map.asteroid.BaseAsteroid
 */
public class BaseAsteroid extends Asteroid {

    /**
     * Stores the Resources that are needed to win the game.
     */
    public static final BluePrintResources winConditionResources;
    private static final long serialVersionUID = -1707774486046615795L;

    static {
        //To win the game, three of every type of resource are needed
        winConditionResources = new BluePrintResources();
        for (int i = 0; i < 3; i++) {
            winConditionResources.addResources(new Iron());
            winConditionResources.addResources(new Uranium());
            winConditionResources.addResources(new Coal());
            winConditionResources.addResources(new Ice());
        }
    }

    public BaseAsteroid() {
        name = "BASE";
        depth = 0;
        inPerihelion = false;
    }

    /**
     * Accepts an entity and checks if the game is won.
     *
     * @param traveller it will be added to the list of entities.
     */
    public void acceptTraveller(Traveller traveller) {
        this.travellers.add(traveller);

        ArrayList<Resource> resourcesOnAsteroid = new ArrayList<>();

        // We need to know the quantity of the resources that are on the baseAsteroid.
        for (Traveller value : travellers) {
            if (value.getResources() != null)
                resourcesOnAsteroid.addAll(value.getResources());
        }

        if (winConditionResources.isBuildable(resourcesOnAsteroid))
            Game.getInstance().gameWon();
    }

    /**
     * Overridden method, because the Base Asteroid should not be in perihelion.
     */
    @Override
    public void changePerihelionState() {
    }
}
