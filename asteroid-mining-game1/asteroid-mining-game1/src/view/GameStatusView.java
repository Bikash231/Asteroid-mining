package view;

import control.Game;

import javax.swing.*;
import java.awt.*;

/**
 * UI panel showing the data of the game's status
 */
public class GameStatusView extends JPanel {
    /**
     * A JLabel displaying the current round number.
     */
    private final JLabel round;
    /**
     * A JLabel shown if sunflare is coming.
     */
    private final JLabel sunflare;

    /**
     * Constructor, initializes the UI components of this panel.
     */
    public GameStatusView() {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.setBorder(BorderFactory.createEmptyBorder(5, 8, 5, 8));
        setFont(getFont().deriveFont(22.0f));

        JButton saveBtn = new JButton("Save game");
        saveBtn.addActionListener(e -> Game.getInstance().saveData());
        add(saveBtn);
        add(Box.createHorizontalGlue());

        round = new JLabel();
        round.setFont(getFont());
        add(round);
        sunflare = new JLabel();
        sunflare.setFont(getFont().deriveFont(Font.BOLD));
        sunflare.setForeground(Color.RED);
        sunflare.setText("sunflare is coming");
        add(sunflare);
        add(Box.createHorizontalGlue());

        JButton centerBtn = new JButton("Center view");
        centerBtn.addActionListener(e -> GameWindow.currentSettlerChanged(null, Game.getInstance().getCurrentSettler()));
        add(centerBtn);
    }

    /**
     * Refreshes the data shown by this panel
     *
     * @param round          The current round of the game.
     * @param sunflareComing Whether a sunflare is coming or not.
     */
    public void updateView(Integer round, boolean sunflareComing) {
        this.round.setText("Round " + round.toString());
        sunflare.setVisible(sunflareComing);
    }
}
