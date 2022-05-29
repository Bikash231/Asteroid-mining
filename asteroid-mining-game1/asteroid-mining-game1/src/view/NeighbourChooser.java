package view;

import map.asteroid.Asteroid;
import map.asteroid.Neighbours;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * A dialog which is used for selecting an Asteroid from a Neighbour object
 */
public class NeighbourChooser extends JDialog {

    private final ArrayList<NeighbourAsteroid> neighboursArray;
    private Asteroid selectedAsteroid = null;
    private JList<NeighbourAsteroid> neighboursList;

    /**
     * Constructor, initializes the data and UI of the dialog
     *
     * @param neighbours the Neighbours object from which the user can chose an Asteroid
     */
    public NeighbourChooser(Neighbours neighbours) {
        super(GameWindow.getInstance(), "Choose a neighbour", true);
        setMinimumSize(new Dimension(220, 80));
        setResizable(false);

        neighboursArray = new ArrayList<>();
        for (Asteroid a : neighbours.getAsteroidNeighbours())
            if (a != null)
                neighboursArray.add(new NeighbourAsteroid(a, false));
        for (Asteroid a : neighbours.getTeleportGateNeighbours())
            if (a != null)
                neighboursArray.add(new NeighbourAsteroid(a, true));

        initDialog();
        this.pack();
        setLocationRelativeTo(GameWindow.getInstance());
    }

    /**
     * Initializes the dialog UI.
     */
    private void initDialog() {
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        add(Box.createRigidArea(new Dimension(0, 10)));
        neighboursList = new JList<>(
                new AbstractListModel<>() {
                    public int getSize() {
                        return neighboursArray.size();
                    }

                    public NeighbourAsteroid getElementAt(int index) {
                        return neighboursArray.get(index);
                    }
                });
        neighboursList.setFixedCellWidth(160);
        ((DefaultListCellRenderer) neighboursList.getCellRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
        add(neighboursList);
        add(Box.createRigidArea(new Dimension(0, 10)));

        JPanel applyPanel = new JPanel();
        JButton okBtn = new JButton("Ok");
        okBtn.addActionListener(new ClickOk());
        applyPanel.add(okBtn);
        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.addActionListener(e -> dispose());
        applyPanel.add(cancelBtn);
        add(applyPanel);
    }

    /**
     * Shows the chooser dialog
     *
     * @return the Asteroid object chosen by the user
     */
    public Asteroid chooseAsteroid() {
        this.setVisible(true);
        return selectedAsteroid;
    }

    /**
     * Internal class to represent the JList data type
     */
    static class NeighbourAsteroid {
        public final Asteroid asteroid;
        private final boolean throughTeleport;

        /**
         * Constructor, sets the fields to the supplied values
         *
         * @param a               the Asteroid represented by this object
         * @param throughTeleport whether this Asteroid is only a Neighbour because of a teleport gate
         */
        public NeighbourAsteroid(Asteroid a, boolean throughTeleport) {
            asteroid = a;
            this.throughTeleport = throughTeleport;
        }

        /**
         * Overridden toString, used by the JList to draw the cells
         *
         * @return The name of the stored Asteroid, and an indication if its accessible through a teleport gate
         */
        @Override
        public String toString() {
            return asteroid.getName() + (throughTeleport ? " (teleport)" : "");
        }
    }

    /**
     * ActionListener for the click of the OK button
     * Sets the selected asteroid as current, and closes the dialog
     */
    final class ClickOk implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            selectedAsteroid = neighboursList.getSelectedValue().asteroid;
            dispose();
        }
    }
}
