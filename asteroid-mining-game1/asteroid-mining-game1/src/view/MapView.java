package view;

import control.Game;
import map.Map;
import map.asteroid.Asteroid;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

public class MapView extends JPanel {
    /**
     * Map field, for accessing the asteroids, and their asteroidViews.
     */
    private final Map map = Game.getInstance().getMap();

    /**
     * Height of the map (panel).
     */
    private final int height = 3000;

    /**
     * Width of the map (panel).
     */
    private final int width = 3000;

    /**
     * Determine which is the closest distance between two neighbouring asteroidView.
     */
    private final int gap = 30;

    /**
     * AsteroidViews are generated in an imaginary box(container),
     * which means the distances between the asteroidViews are different.
     * Container size describe the height and width (same value) of the box.
     */
    private final int containerSize = 300;

    /**
     * Describe how many pixels will the height and width of the teleport be.
     */
    private final int teleportSize = 25;

    /**
     * Describe how many pixels will the height and width of the asteroid(View) be.
     */
    private final int asteroidSize = 100;

    /**
     * Random generator object, being used in generation of the asteroids positions.
     */
    private final Random rnd = new Random();


    /**
     * Constructor of the MapView. Sets the asteroids in position, connect the asteroids etc.
     */

    public MapView() {
        this.setPreferredSize(new Dimension(width, height));
        this.setLayout(null);



        // Delta variables change where the asteroids being placed.
        int dX = 0;
        int dY = 0;
        for (Asteroid a : map.getAsteroids()) {
            AsteroidView av = a.getAsteroidView();

            // Setting random position for the asteroidView in an imaginary container, and adding them to the panel.
            int x = rnd.nextInt(containerSize - (gap + teleportSize + asteroidSize)) + dX;
            int y = rnd.nextInt(containerSize - (gap + teleportSize + asteroidSize)) + dY;
            av.setBounds(x, y, asteroidSize, asteroidSize);
            add(av);
            dX += containerSize;
            if (dX >= width) {
                dY += containerSize;
                dX = 0;
            }
        }

        MouseAdapter mouseAdapter = new MouseAdapter() {
            private Point origin;

            @Override
            public void mousePressed(MouseEvent e) {
                origin = new Point(e.getPoint());
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (origin != null) {
                    JViewport viewPort = (JViewport) SwingUtilities.getAncestorOfClass(JViewport.class, MapView.this);
                    if (viewPort != null) {
                        int deltaX = origin.x - e.getX();
                        int deltaY = origin.y - e.getY();

                        Rectangle view = viewPort.getViewRect();
                        view.x += deltaX;
                        view.y += deltaY;

                        MapView.this.scrollRectToVisible(view);
                    }
                }
            }
        };

        this.setAutoscrolls(true);
        this.addMouseListener(mouseAdapter);
        this.addMouseMotionListener(mouseAdapter);
    }

    /**
     * Draws the neighbour connections and the teleport gates.
     *
     * @param g Graphics object of the map(panel)
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawNeighboursConnection(g);

    }

    /**
     * Draws the neighbour connections.
     *
     * @param g Graphics object of the map(panel)
     */
    private void drawNeighboursConnection(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setStroke(new BasicStroke(3));
        for (Asteroid a : map.getAsteroids()) {
            g2.setColor(Color.BLACK);
            for (Asteroid b : a.getListOfNeighbours().getAsteroidNeighbours()) {
                drawNeighbourLine(g2, a, b);
            }
            g2.setColor(Color.GRAY);
            for (Asteroid b : a.getListOfNeighbours().getTeleportGateNeighbours()) {
                drawNeighbourLine(g2, a, b);
            }
        }
    }

    /**
     * Draws a neighbour line
     *
     * @param g2 graphics object to draw the line to
     * @param a  first Asteroid of line
     * @param b  second Asteroid of line
     */
    private void drawNeighbourLine(Graphics2D g2, Asteroid a, Asteroid b) {
        int x1 = a.getAsteroidView().getCenter().x;
        int y1 = a.getAsteroidView().getCenter().y;
        int x2 = b.getAsteroidView().getCenter().x;
        int y2 = b.getAsteroidView().getCenter().y;
        g2.drawLine(x1, y1, x2, y2);
    }

    /**
     * Updates the map(panel), repaints it.
     */
    public void updateView(Asteroid asteroid) {
        remove(asteroid.getAsteroidView());
        repaint();
    }
}
