package view;

import map.asteroid.Asteroid;

import javax.swing.*;
import java.awt.*;

public class AsteroidStatusView extends JPanel {
    /**
     * Label for asteroid's name.
     */
    private final JLabel titleLabel = new JLabel();
    /**
     * Text area for asteroid's details.
     */
    private final JTextArea detailsTextArea = new JTextArea();

    /**
     * Creates an AsteroidStatusView Object
     */
    public AsteroidStatusView() {
        titleLabel.setFont(titleLabel.getFont().deriveFont(28.0f));
        detailsTextArea.setFont(detailsTextArea.getFont().deriveFont(20.0f));
        add(titleLabel);
        add(detailsTextArea);
        titleLabel.setPreferredSize(new Dimension(205, 50));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setVerticalAlignment(SwingConstants.TOP);
        detailsTextArea.setPreferredSize(new Dimension(205, 450));
        detailsTextArea.setEditable(false);
        detailsTextArea.setHighlighter(null);
        detailsTextArea.setBackground(UIManager.getColor("Panel.background"));
        this.setPreferredSize(new Dimension(230, 500));
    }

    /**
     * Replaces new lines and tabs with their correct html versions.
     *
     * @param orig String to be converted.
     * @return Returns string with replaced elements.
     */
    public static String toMultiline(String orig) {
        return orig.replaceAll("\n", System.lineSeparator()).replaceAll("\t", "      ");
    }

    /**
     * Updates the asteroid's status view.
     *
     * @param asteroid Given asteroid.
     */
    public void updateView(Asteroid asteroid) {
        if (asteroid == null) {
            return;
        }
        updateNameText(asteroid);
        String details = "";
        details += getAsteroidInfo(asteroid);
        details += getEntityInfo(asteroid);
        details += getTeleportInfo(asteroid);
        detailsTextArea.setText(toMultiline(details));
    }

    /**
     * Gets info of teleportgates on asteroid.
     *
     * @param asteroid Given asteroid.
     * @return Returns where the teleportgate goes, if there's any on asteroid.
     */
    private String getTeleportInfo(Asteroid asteroid) {
        String res = "";
        res += "Teleport:\n";
        res += asteroid.getTeleportGate() != null ? (asteroid.getTeleportGate().getOtherSide() != null ? "\t->" +
                asteroid.getTeleportGate().getOtherSide().getName() + "\n" : "\t-\n") : "\t-\n";
        return res;
    }

    /**
     * Gets info of entity on asteroid.
     *
     * @param asteroid Given asteroid.
     * @return Returns with names of entities on asteroid.
     */
    private String getEntityInfo(Asteroid asteroid) {
        StringBuilder res = new StringBuilder();
        res.append("Settlers:\n");
        if (asteroid.getTravellers().size() == 0) {
            res.append("\t-\n");
        } else {
            for (int i = 0; i < asteroid.getTravellers().size(); i++) {
                res.append("\t").append(asteroid.getTravellers().get(i).getName()).append("\n");
            }
        }
        return res.toString();
    }

    /**
     * Gets info of asteroid.
     *
     * @param asteroid Given asteroid.
     * @return Returns with info if asteroid's in perihelion and what resource it has.
     */
    private String getAsteroidInfo(Asteroid asteroid) {
        String res = "";
        res += asteroid.getInPerihelion() ? "In Perihelion\n" : "\n";
        if (!asteroid.getTravellers().isEmpty()) {
            res += "Surface thickness: " + asteroid.getDepth() + "\n";
        }
        res += "Resource: " + (asteroid.getResource() == null ? "-\n" : asteroid.getResource().toString() + "\n");
        return res;
    }

    /**
     * Updates the name when an asteroid is chosen.
     *
     * @param asteroid Given asteroid.
     */
    private void updateNameText(Asteroid asteroid) {
        titleLabel.setText(asteroid.getName());
    }
}
