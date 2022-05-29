package control;

import map.Map;
import map.asteroid.Asteroid;
import map.traveller.Settler;
import map.traveller.Steppable;
import map.traveller.SpaceStation;
import view.GameStatusView;
import view.GameWindow;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * Class control.Game
 */
public final class Game implements Serializable {

    private static final long serialVersionUID = 8288238373621545294L;
    /**
     * The instance of the singleton Game class.
     */
    private static Game instance = new Game();
    /**
     * The panel on which the game displays info about the current round.
     */
    private static GameStatusView statusView;
    /**
     * Reference to the Map object, which contains the game's map.
     */
    private final Map map;
    /**
     * List of Settlers, who are playing the game.
     */
    private final ArrayList<Settler> settlers;
    /**
     * List of Steppables who are playing.
     */
    private final ArrayList<Steppable> steppables;
    /**
     * Counts the number of rounds played.
     */
    private int currentRound;
    /**
     * The number of the round in which the next sunflare occurs.
     */
    private int nextSunStorm;
    /**
     * The settler, who should step next.
     */
    private Settler current;

    /**
     * Constructor. Initializes the Game object.
     */
    private Game() {
        currentRound = 1;
        nextSunStorm = generateNextSunStorm();
        map = new Map();
        settlers = new ArrayList<>();
        steppables = new ArrayList<>();
        instance = this;
    }

    /**
     * Copy constructor.
     *
     * @param game The Game object to copy.
     */
    private Game(Game game) {
        currentRound = game.currentRound;
        nextSunStorm = game.nextSunStorm;
        map = game.map;
        settlers = game.settlers;
        steppables = game.steppables;
        current = game.current;
    }

    /**
     * Adds settlers with the specified names to the settlers list and sets the first settler as current.
     *
     * @param playerNames A String list containing the players' names.
     */
    public static void start(ArrayList<String> playerNames) {
        instance.settlers.clear(); // just to be sure
        for (String playerName : playerNames) {
            Settler settler = new Settler(playerName);
            instance.settlers.add(settler);
            // settlers are placed on the base asteroid at the beginning of the game
            settler.travel(instance.map.getBaseAsteroid());
        }
        // add UFOs
        Random rnd = new Random();
        int ufoCount = rnd.nextInt(11) + 5; // 5 <= n < 16
        for (int i = 0; i < ufoCount; i++) {
            SpaceStation spaceStation = new SpaceStation(String.format("UFO-%02d", i + 1));
            // place the ufo on a random asteroid
            ArrayList<Asteroid> asteroids = instance.map.getAsteroids();
            spaceStation.travel(asteroids.get(rnd.nextInt(asteroids.size())));
            instance.steppables.add(spaceStation);
        }
        GameWindow.init();
        instance.current = instance.settlers.get(0);
        instance.current.yourTurn();
    }

    /**
     * Sets the status view object.
     *
     * @param view The GameStatusView
     */
    public static void setStatusView(GameStatusView view) {
        statusView = view;
        statusView.updateView(instance.currentRound, false);
    }

    /**
     * Other objects can use this method to access the Game object.
     *
     * @return The instance of the Game class.
     */
    public static Game getInstance() {
        return instance;
    }

    /**
     * Loads the previously saved game.
     */
    public static void readDataFromFile() {
        try {
            ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("./save.dat"));
            Game.instance = new Game((Game) inputStream.readObject());
            inputStream.close();
            GameWindow.init();
            instance.current.yourTurn();
            GameWindow.currentSettlerChanged(null, instance.current);
        } catch (FileNotFoundException notFoundException) {
            JOptionPane.showMessageDialog(null, "File cannot be found. Error message: " +
                    notFoundException.getMessage(), "File not found", JOptionPane.WARNING_MESSAGE);
            System.exit(-1);
        } catch (StreamCorruptedException streamCorruptedException) {
            JOptionPane.showMessageDialog(null, "Save file is corrupted. Error message: " +
                    streamCorruptedException.getMessage(), "File corrupted", JOptionPane.ERROR_MESSAGE);
            System.exit(-1);
        } catch (Exception exception) { // IOException, ClassNotFoundException or InvalidClassException
            JOptionPane.showMessageDialog(null, "Error while reading save file. Shutting down." +
                    "Error message: " + exception.getMessage(), "Error in save file", JOptionPane.ERROR_MESSAGE);
            System.exit(-1);
        }
    }

    /**
     * Returns the map the game is played on.
     *
     * @return The game's Map object.
     */
    public Map getMap() {
        return map;
    }

    /**
     * Returns the player who should step next.
     *
     * @return The current settler.
     */
    public Settler getCurrentSettler() {
        return current;
    }

    /**
     * Remove a Settler object from settlers
     */
    public void removeSettler(Settler settler) {
        if (settlers.size() <= 1) {
            gameLost();
            return;
        }
        if (settler == current)
            nextPlayer();
        settlers.remove(settler);
    }

    /**
     * Add a Steppable object to the steppables list (if the object is not already in it)
     *
     * @param newSteppable The steppable object that should be added to the list
     */
    public void addSteppable(Steppable newSteppable) {
        if (!steppables.contains(newSteppable))
            steppables.add(newSteppable);
    }

    /**
     * Remove a Steppable object from steppables
     */
    public void removeSteppable(Steppable steppable) {
        steppables.remove(steppable);
    }

    /**
     * This method is called when the settlers have collected all the needed resources on a single asteroid.
     */
    public void gameWon() {
        JOptionPane.showMessageDialog(GameWindow.getInstance(), "Settlers won!");
    }

    /**
     * This method is called if there's no way for the players to win.
     */
    private void gameLost() {
        JOptionPane.showMessageDialog(GameWindow.getInstance(), "Settlers lost!");
        GameWindow.getActionsView().updateView(null);
    }

    /**
     * Randomly generates a number when a sunflare should occur.
     *
     * @return int The number of round in which the next sunflare occurs.
     */
    private int generateNextSunStorm() {
        Random rnd = new Random();
        if (currentRound < 20) {
            return rnd.nextInt(2) + 20;
        }
        return rnd.nextInt(20) + 10;
    }

    /**
     * This method is called every time all the settlers have moved in a given round. It steps with all the steppables,
     * checks if there should be a sunflare, and changes the asteroid's 'inPerihelion' state.
     */
    private void roundFinished() {
        Random random = new Random();
        if (currentRound == nextSunStorm) {
            map.sunflare();
            nextSunStorm = generateNextSunStorm() + currentRound;
        }
        for (Steppable steppable : steppables) {
            steppable.step();
        }
        if (random.nextInt(4) == 0) // generates a random number, where 0 <= n < 4
            map.changePerihelion();

        if (settlers.size() == 0)
            gameLost();

        currentRound++;
        current = settlers.get(0);

        statusView.updateView(currentRound, nextSunStorm == currentRound + 1);
        map.roundPassed();  // resource exposition
    }

    /**
     * A Settler calls this method after he stepped in a given round.
     */
    public void nextPlayer() {
        Settler previousSettler = current;
        int idx = settlers.indexOf(current);
        if (idx == settlers.size() - 1) {
            roundFinished();
        } else {
            current = settlers.get(idx + 1);
        }
        GameWindow.currentSettlerChanged(previousSettler, current);
        current.yourTurn();
    }

    /**
     * Saves the game's current state.
     */
    public void saveData() {
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("./save.dat"));
            outputStream.writeObject(this);
            outputStream.close();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Game session cannot be saved ("
                    + ex.getMessage() + ')', "Error saving game", JOptionPane.ERROR_MESSAGE);
        }
    }
}
