package view;

import control.ActionFailedException;
import control.Game;
import map.asteroid.Asteroid;
import map.traveller.Settler;
import map.resource.Resource;

import javax.swing.*;

public class SettlerActionsView extends JPanel {
    private final JButton placeTeleportBtn;
    private Settler settler;

    SettlerActionsView() {
        JButton moveBtn = new JButton("Move");
        moveBtn.addActionListener(actionEvent -> {
            NeighbourChooser chooser = new NeighbourChooser(settler.getAsteroid().getListOfNeighbours());
            Asteroid chosen = chooser.chooseAsteroid();
            if (chosen != null)
                settler.travel(chosen);
        });

        JButton drillBtn = new JButton("Drill");
        drillBtn.addActionListener(actionEvent -> {
            try {
                settler.drill();
            } catch (ActionFailedException e) {
                JOptionPane.showMessageDialog(GameWindow.getInstance(), e.getMessage());
            }
        });

        JButton mineBtn = new JButton("Mine");
        mineBtn.addActionListener(actionEvent -> {
            try {
                settler.mine();
            } catch (ActionFailedException e) {
                JOptionPane.showMessageDialog(GameWindow.getInstance(), e.getMessage());
            }
        });

        this.placeTeleportBtn = new JButton("Place Teleport");
        placeTeleportBtn.addActionListener(actionListener -> settler.placeTeleportationGate());

        JButton buildRobotBtn = new JButton("Build Robot");
        buildRobotBtn.addActionListener(actionListener -> {
            try {
                settler.buildRobot();
            } catch (ActionFailedException e) {
                JOptionPane.showMessageDialog(GameWindow.getInstance(), e.getMessage());
            }
        });

        JButton buildTeleportBtn = new JButton("Build Teleport");
        buildTeleportBtn.addActionListener(actionEvent -> {
            try {
                settler.buildTeleportationGate();
            } catch (ActionFailedException e) {
                JOptionPane.showMessageDialog(GameWindow.getInstance(), e.getMessage());
            }
        });

        JButton placeBtn = new JButton("Fill Asteroid");
        placeBtn.addActionListener(actionEvent -> {
            try {
                if (settler.getResources().isEmpty()) {
                    JOptionPane.showMessageDialog(GameWindow.getInstance(),
                            "There is no resource in the inventory, nothing to place back!");
                    return;
                }
                ResourceChooser rc = new ResourceChooser(settler.getResources());
                Resource res = rc.chooseResource();
                if (res != null)
                    settler.fillAsteroid(res);
            } catch (ActionFailedException e) {
                JOptionPane.showMessageDialog(GameWindow.getInstance(), e.getMessage());
            }
        });

        JButton passBtn = new JButton("Pass");
        passBtn.addActionListener(actionListener -> Game.getInstance().nextPlayer());

        buildRobotBtn.setToolTipText("Required resources: Uranium: 1, Coal: 1, Iron: 1");
        buildTeleportBtn.setToolTipText("Required resources: Uranium: 1, Ice: 1, Iron: 2");

        add(moveBtn);
        add(drillBtn);
        add(mineBtn);
        add(placeBtn);
        add(buildTeleportBtn);
        add(placeTeleportBtn);
        add(buildRobotBtn);
        add(passBtn);
    }


    public void updateView(Settler settler) {
        if (settler == null) { // used if the game ended
            this.setVisible(false);
            return;
        } else this.setVisible(true);

        this.settler = settler;

        // if the settler doesn't have at least one teleport gate or the asteroid he's standing on already has a
        // teleport, the place teleport button is disabled
        this.placeTeleportBtn.setEnabled(settler.getTeleportationGateNumber() != 0 && settler.getAsteroid().getTeleportGate() == null);
    }
}
