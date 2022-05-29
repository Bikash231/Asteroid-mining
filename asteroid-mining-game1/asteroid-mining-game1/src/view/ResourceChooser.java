package view;

import map.resource.Resource;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ResourceChooser extends JDialog {

    private final JList<Resource> resourceList;
    private Resource selectedResource = null;

    public ResourceChooser(ArrayList<Resource> resources) {
        super(GameWindow.getInstance(), "Choose a resource", true);
        setMinimumSize(new Dimension(220, 120));
        setResizable(false);
        resourceList = new JList<>(resources.toArray(new Resource[0]));
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
        resourceList.setFixedCellWidth(160);
        ((DefaultListCellRenderer) resourceList.getCellRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
        add(resourceList);
        add(Box.createRigidArea(new Dimension(0, 10)));

        JPanel applyPanel = new JPanel();
        JButton okBtn = new JButton("Ok");
        okBtn.addActionListener(e -> {
            dispose();
            selectedResource = resourceList.getSelectedValue();
        });
        applyPanel.add(okBtn);
        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.addActionListener(e -> dispose());
        applyPanel.add(cancelBtn);
        add(applyPanel);
    }

    /**
     * Shows the chooser dialog
     *
     * @return the Resource object chosen by the user
     */
    public Resource chooseResource() {
        this.setVisible(true);
        return selectedResource;
    }
}
