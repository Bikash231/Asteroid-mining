package view;

import control.Game;
import map.traveller.Settler;

import javax.swing.*;
import java.awt.*;

/**
 * The game's main window.
 */
public class GameWindow extends JFrame {
    /**
     * The instance of the singleton GameWindow class.
     */
    private static final GameWindow instance = new GameWindow();

    /**
     * A subclass of JPanel containing buttons of actions a settler may perform.
     */
    private final SettlerActionsView actionsView;

    /**
     * Displays the content of the current settler's inventory.
     */
    private final SettlerInventoryView inventoryView;

    /**
     * Displays the details of the selected asteroid.
     */
    private final AsteroidStatusView asteroidStatusView;

    /**
     * Displays the current round number. Also, notifies the player if a sunflare is coming in the next round.
     */
    private final GameStatusView gameStatusView;

    /**
     * A subclass of JPanel containing the game's map. Displays the asteroids.
     */
    private final MapView mapView;

    /**
     * The scroll pane that contains the map view.
     */
    private final JScrollPane mapScrollPane;

    /**
     * Creates a GameWindow object.
     */
    private GameWindow() {
        super("Asteroid mining game");
        Dimension windowSize = new Dimension(1280, 720);
        this.setPreferredSize(windowSize);
        this.setMinimumSize(windowSize);
        this.setLocationRelativeTo(null); // place the window in the center of the screen
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());

        mapView = new MapView();
        this.mapScrollPane = new JScrollPane(mapView);
        this.mapScrollPane.setViewport(new ImageViewport());
        this.mapScrollPane.setViewportView(mapView);
        mapScrollPane.getVerticalScrollBar().setUnitIncrement(20);
        mapScrollPane.getHorizontalScrollBar().setUnitIncrement(20);
        this.add(mapScrollPane, BorderLayout.CENTER);

        gameStatusView = new GameStatusView();
        this.add(gameStatusView, BorderLayout.NORTH);

        actionsView = new SettlerActionsView();
        this.add(actionsView, BorderLayout.SOUTH);

        inventoryView = new SettlerInventoryView();
        this.add(inventoryView, BorderLayout.WEST);

        asteroidStatusView = new AsteroidStatusView();
        this.add(asteroidStatusView, BorderLayout.EAST);

        // maximize window
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.pack();
    }

    /**
     * Starts the game.
     */
    public static void init() {
        instance.setVisible(true);

        Game.setStatusView(instance.gameStatusView);
    }

    /**
     * This method can be used to access the main window object outside this class.
     *
     * @return The instance of this singleton class.
     */
    public static GameWindow getInstance() {
        return instance;
    }

    /**
     * Gets the singleton class' AsteroidStatusView object.
     *
     * @return The AsteroidStatusView used throughout the game.
     */
    public static AsteroidStatusView getAsteroidStatusView() {
        return instance.asteroidStatusView;
    }

    /**
     * Gets the SettlerActionsView object.
     *
     * @return The SettlerActionsView used throughout the game.
     */
    public static SettlerActionsView getActionsView() {
        return instance.actionsView;
    }

    /**
     * Gets the SettlerInventoryView object.
     *
     * @return The SettlerInventoryView used throughout the game.
     */
    public static SettlerInventoryView getInventoryView() {
        return instance.inventoryView;
    }

    public static MapView getMapView() {
        return instance.mapView;
    }

    /**
     * Updates the GUI so that the components display correctly after the current settler changes.
     *
     * @param previous The previous settler.
     * @param current  The current settler.
     */
    public static void currentSettlerChanged(Settler previous, Settler current) {
        // the previous settler's asteroid should not be painted light pink, so we repaint it
        if (previous != null) {
            previous.getAsteroid().getAsteroidView().updateView();
        }
        // update the current settler's asteroid's view so that its painted in light pink
        current.getAsteroid().getAsteroidView().updateView();
        // update the status view to display info about the asteroid the current settler is standing on
        GameWindow.getAsteroidStatusView().updateView(current.getAsteroid());

        // center the current asteroid
        JViewport viewport = instance.mapScrollPane.getViewport();
        int width = viewport.getWidth();
        int height = viewport.getHeight();
        Point viewportCenter = new Point(current.getAsteroid().getAsteroidView().getCenter().x - width / 2,
                current.getAsteroid().getAsteroidView().getCenter().y - height / 2);
        viewport.setViewPosition(viewportCenter);
        instance.mapScrollPane.repaint();
    }
}
