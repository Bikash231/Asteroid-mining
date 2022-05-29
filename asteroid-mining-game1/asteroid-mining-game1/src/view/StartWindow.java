package view;

import control.Game;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class StartWindow extends JFrame {

    /**
     * Constructor, initializes the Frame
     */
    public StartWindow() {
        super("New game");
        setLayout(new BorderLayout());
        Dimension windowSize = new Dimension(860, 620);
        setPreferredSize(windowSize);
        setMinimumSize(windowSize);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        JLabel background=new JLabel(new ImageIcon("C:\\Users\\akmatsad\\Downloads\\asteroid-mining-game1\\src" +
                "\\view\\space.jpg"));


        background.setLayout(new BorderLayout());
        // title labels
        JPanel panel = new JPanel(new BorderLayout(25, 25));

        JLabel centerTitleLabel = new JLabel("Welcome to the Asteroid Mining Game!");
        centerTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        centerTitleLabel.setFont(centerTitleLabel.getFont().deriveFont(36.0f));
        JLabel centerSubtitleLabel = new JLabel("Input players' names");
        centerSubtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        centerSubtitleLabel.setFont(centerSubtitleLabel.getFont().deriveFont(24.0f));
        JPanel northPanel = new JPanel();
        northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));
        northPanel.setBorder(BorderFactory.createEmptyBorder(40, 0, 0, 0));
        JPanel north1 = new JPanel();
        JPanel north2 = new JPanel();
        north2.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        north1.add(centerTitleLabel);
        north2.add(centerSubtitleLabel);
        northPanel.add(north1);
        northPanel.add(north2);
        // northPanel.add(background);
        panel.setBorder(BorderFactory.createEmptyBorder(60, 100, 80, 100));
        // name text fields and labels
        JPanel labels = new JPanel(new GridLayout(0, 1, 2, 2));
        JPanel input = new JPanel(new GridLayout(0, 1, 2, 2));
        ArrayList<JTextField> textFields = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            JLabel label = new JLabel(String.format("Settler %d:", i + 1));
            label.setFont(label.getFont().deriveFont(20.0f));
            labels.add(label);
            JTextField textField = new JTextField(15);
            textField.setFont(textField.getFont().deriveFont(20.0f));
            textFields.add(textField);
            input.add(textField);
        }
        panel.add(labels, BorderLayout.WEST);

        /* buttons on the bottom (south panel) */
        JPanel southPanel = new JPanel();
        southPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        southPanel.setLayout(new BorderLayout());
        JPanel startPanel = new JPanel();
        // load button
        JButton loadGameBtn = new JButton("Load game");
        Font buttonFont = loadGameBtn.getFont().deriveFont(16.0f);
        loadGameBtn.setFont(buttonFont);
        loadGameBtn.addActionListener(e -> {
            Game.readDataFromFile();
            setVisible(false);
        });
        startPanel.add(loadGameBtn);
        // start button
        JButton startGameButton = new JButton("Start game");
        startGameButton.setFont(buttonFont);
        startPanel.add(startGameButton);
        startGameButton.addActionListener(actionEvent -> {
            ArrayList<String> playerNames = new ArrayList<>();
            for (JTextField textField : textFields) {
                String text = textField.getText().trim();
                if (!text.equals(""))
                    playerNames.add(text);
            }
            // if not enough player names have been entered, the game shouldn't start
            if (playerNames.size() < 2) {
                JOptionPane.showMessageDialog(this, "Please input at least two valid names.");
                return;
            }
            // hide the start screen, so that no new game can be started when the main window is active
            setVisible(false);
            Game.start(playerNames);
        });
        southPanel.add(startPanel, BorderLayout.EAST);

        // add everything and show
        panel.add(southPanel, BorderLayout.SOUTH);
        panel.add(input, BorderLayout.CENTER);

        // try{
        //     BufferedImage img = ImageIO.read(new File("C:\\Users\\akmatsad\\Downloads\\asteroid-mining-game1\\src" +
        //             "\\view\\space.jpg"));
        //     this.setContentPane(new JLabel(new ImageIcon(img)));
        // } catch (IOException exp) {
        //     exp.printStackTrace();
        // }
        background.add(panel, BorderLayout.CENTER);

        background.add(northPanel, BorderLayout.NORTH);
        add(background);
        pack();

    }



    /**
     * Shows the start window
     */
    public void startGame() {
        setVisible(true);
    }
}
