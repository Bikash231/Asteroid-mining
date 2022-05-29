package view;

import map.traveller.Settler;
import map.resource.*;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class SettlerInventoryView extends JPanel {

    //Label for settler's information
    private final JTextArea informationLabel;

    //Label for settler's name
    private final JLabel titleLabel;

    /**
     * Creates a SettlerInventoryView Object
     */
    SettlerInventoryView() {
        titleLabel = new JLabel();
        informationLabel = new JTextArea();
        titleLabel.setFont(titleLabel.getFont().deriveFont(28.0f));
        informationLabel.setFont(informationLabel.getFont().deriveFont(20.0f));
        setPreferredSize(new Dimension(200, 500));
        add(titleLabel);
        add(informationLabel);
        titleLabel.setPreferredSize(new Dimension(180, 50));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setVerticalAlignment(SwingConstants.TOP);
        informationLabel.setPreferredSize(new Dimension(180, 170));
        informationLabel.setEditable(false);
        informationLabel.setHighlighter(null);
        informationLabel.setBackground(UIManager.getColor("Panel.background"));
    }

    /**
     * @param currentSettler the settler whose information will be shown
     */
    public void updateView(Settler currentSettler) {
        titleLabel.setText(currentSettler.getName());
        informationLabel.setText("");

        HashMap<String, Integer> resCount = new HashMap<>();
        resCount.put((new Uranium()).toString(), 0);
        resCount.put((new Coal()).toString(), 0);
        resCount.put((new Ice()).toString(), 0);
        resCount.put((new Iron()).toString(), 0);
        for (Resource r : currentSettler.getResources()) {
            int countSoFar = resCount.get(r.toString());
            resCount.put(r.toString(), countSoFar + 1);
        }
        resCount.forEach((k, v) -> informationLabel.append(k + ": " + v + '\n'));

        informationLabel.append("\nTeleports: " + currentSettler.getTeleportationGateNumber());
    }

}
