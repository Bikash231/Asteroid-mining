package map;

import map.asteroid.Asteroid;
import map.asteroid.BaseAsteroid;
import map.resource.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

/**
 * Class Map
 */
public class Map implements Serializable {
    private static final long serialVersionUID = -4586535826222086282L;
    /**
     * Random object, for random generation.
     */
    private final Random rnd = new Random();
    /**
     * The asteroids in the map.
     */
    private final ArrayList<Asteroid> asteroids = new ArrayList<>();
    /**
     * The baseasteroid, where all settlers must be to win the game (with enough resources).
     */
    private final Asteroid baseAsteroid;

    /**
     * Constructor of the map, this will initialize the asteroids, and neighbour connections.
     */
    public Map() {
        /*
         * It's declare how many "row" or "column" will there be.
         * For example mapBound=10 means that will be total of 10x10=100 asteroids.
         */
        int mapBound = 10; // should be at least 4
        baseAsteroid = new BaseAsteroid();
        int centerIndex = mapBound / 2;

        // 2D array, which is being used in asteroid generations, and in connecting them as neighbours.
        Asteroid[][] asteroidsMatrix = new Asteroid[mapBound][mapBound];
        asteroidsMatrix[centerIndex][centerIndex] = baseAsteroid;

        int resLength = mapBound * mapBound;
        Resource[] resources = new Resource[resLength];
        // generate minimum number of resources (3 each)
        int baseAsteroidIndex = centerIndex * (mapBound + 1);
        for (int i = 0; i < 3 * 4; i++) {
            // generate asteroid index
            int idx = rnd.nextInt(resLength);
            while (idx == baseAsteroidIndex || resources[idx] != null)  // generate new random numbers if the index is already used
                idx = rnd.nextInt(resLength);
            resources[idx] = generateResource(i % 4);   // save resource
        }
        for (int i = 0; i < resLength; i++) {
            if (resources[i] == null && i != baseAsteroidIndex)
                // if the resource is not already added, generate a new one
                resources[i] = generateResource(rnd.nextInt(5));
        }


        // Generation of the asteroids.
        for (int i = 0; i < mapBound; i++) {
            for (int j = 0; j < mapBound; j++) {
                if ((i == centerIndex && j == centerIndex)) {
                    asteroids.add(baseAsteroid);
                } else {
                    int resIdx = mapBound * i + j;
                    Asteroid a = new Asteroid("A" + i + "" + j, rnd.nextBoolean(), rnd.nextInt(6) + 1, resources[resIdx]);
                    asteroidsMatrix[i][j] = a;
                    asteroids.add(a);
                }
            }
        }

        /*
         * Connects the asteroids as neighbours. The asteroids are connected only with
         * the upper, lower, right, left side asteroids.
         */
        for (int i = 0; i < mapBound; i++) {
            for (int j = 0; j < mapBound; j++) {
                /*
                 * The purpose of the try-catches is to connect the upper, lower rows,
                 * and side columns asteroids with each other. IndexOutOfBounds exceptions are ignored for purpose.
                 */
                if (i != 0)
                    asteroidsMatrix[i][j].addNeighbour(asteroidsMatrix[i - 1][j]);
                if (i < mapBound - 1)
                    asteroidsMatrix[i][j].addNeighbour(asteroidsMatrix[i + 1][j]);
                if (j != 0)
                    asteroidsMatrix[i][j].addNeighbour(asteroidsMatrix[i][j - 1]);
                if (j < mapBound - 1)
                    asteroidsMatrix[i][j].addNeighbour(asteroidsMatrix[i][j + 1]);
            }
        }

        // Gives an optional diagonal edge to the neighbours.
        for (int i = 0; i < mapBound; i++) {
            for (int j = 0; j < mapBound; j++) {
                if (rnd.nextBoolean()) {
                    if (i != 0 && j != 0)
                        addDiagonal(asteroidsMatrix[i][j], asteroidsMatrix[i - 1][j - 1]);
                    if (i != 0 && j < mapBound - 1)
                        addDiagonal(asteroidsMatrix[i][j], asteroidsMatrix[i - 1][j + 1]);
                    if (i < mapBound - 1 && j != 0)
                        addDiagonal(asteroidsMatrix[i][j], asteroidsMatrix[i + 1][j - 1]);
                    if (i < mapBound - 1 && j  < mapBound - 1)
                        addDiagonal(asteroidsMatrix[i][j], asteroidsMatrix[i + 1][j + 1]);

                    // Removes a random edge from the current asteroid.
                    int neighboursSize = asteroidsMatrix[i][j].getListOfNeighbours().getAsteroidNeighbours().size();
                    Asteroid temp = asteroidsMatrix[i][j].getListOfNeighbours().getAsteroidNeighbours().get(rnd.nextInt(neighboursSize));
                    asteroidsMatrix[i][j].getListOfNeighbours().getAsteroidNeighbours().remove(temp);
                    temp.getListOfNeighbours().getAsteroidNeighbours().remove(asteroidsMatrix[i][j]);
                }
            }
        }
    }


    /**
     * Adds an additional neighbour to the current one in diagonal represented in a matrix.
     *
     * @param a First asteroid.
     * @param b Second asteroid.
     */
    private void addDiagonal(Asteroid a, Asteroid b) {
        if (!(a.getListOfNeighbours().getAsteroidNeighbours().contains(b) && b.getListOfNeighbours().getAsteroidNeighbours().contains(a))) {
            a.addNeighbour(b);
            b.addNeighbour(a);
        }
    }


    /**
     * Returns a new Resource object based on the passed number.
     *
     * @param i resource id number (0-Uranium, 1-Coal, 2-Iron, 3-Ice, 4+-null)
     * @return the new resource object
     */
    private Resource generateResource(int i) {
        switch (i) {
            case 0:
                return new Uranium();
            case 1:
                return new Coal();
            case 2:
                return new Iron();
            case 3:
                return new Ice();
            default:
                return null;
        }
    }

    /**
     * Default getter of the BaseAsteroid, not used in the test.
     *
     * @return baseAsteroid
     */
    public Asteroid getBaseAsteroid() {
        return baseAsteroid;
    }

    /**
     * Getter of the asteroid list, not used in the test.
     *
     * @return List of Asteroids objects held by asteroidsVector
     */
    public ArrayList<Asteroid> getAsteroids() {
        return asteroids;
    }

    /**
     * Removes an asteroid from the asteroids list.
     *
     * @param asteroid need to remove from the asteroids list.
     */
    public void removeAsteroid(Asteroid asteroid) {
        asteroids.remove(asteroid);
    }

    /**
     * Spreads sunflare all across the asteroids in the list.
     */
    public void sunflare() {
        for (Asteroid a : asteroids) {
            if (rnd.nextInt(100) < 20) {
                a.hitBySunflare();
            }
        }
    }

    /**
     * Calls changePerihelionState function in some asteroids at the end of the round.
     */
    public void changePerihelion() {
        for (Asteroid a : asteroids) {
            if (rnd.nextInt(100) < 40) {
                a.changePerihelionState();
            }
        }
    }

    /**
     * Tells the map that a round has passed
     * Exposes the necessary resource on the map
     */
    public void roundPassed() {
        for (Asteroid a : asteroids) {
            a.expose();
        }
    }
}
