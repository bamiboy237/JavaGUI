import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class CarSalesJPanel extends JPanel implements ActionListener {

    JTextArea buildText;
    JButton buildButton;

    JComboBox<String> makeMenu;
    JComboBox<String> modelMenu;

    String[] makeList;
    String[][] modelList;

    JCheckBox option_stereo, option_selfdrive, option_eject;

    JRadioButton color_red, color_black, color_silver;
    JRadioButton trans_manual, trans_auto;

    ButtonGroup colorGroup, transGroup;

    public CarSalesJPanel() {
        this.setLayout(new BorderLayout(10, 10));
        this.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        this.setPreferredSize(new Dimension(900, 500));

        JPanel titlePanel = new JPanel();
        JPanel carPanel = new JPanel();
        JPanel optionsPanel = new JPanel();
        JPanel colorPanel = new JPanel();
        JPanel transPanel = new JPanel();
        JPanel buildPanel = new JPanel();

        JPanel centerPanel = new JPanel();

        Font appFontLarge = new Font("Georgia", Font.BOLD, 28);
        Font appFontSmall = new Font("Georgia", Font.PLAIN, 16);

        // title
        JLabel titleLabel = new JLabel("Car Sales");
        titleLabel.setFont(appFontLarge);
        titlePanel.add(titleLabel);

        // Make and Model Selection
        makeList = new String[] {
            "Ford",
            "Toyota",
            "Chevy",
            "Honda",
            "BMW",
            "Dodge",
            "Nissan",
            "Jeep",
        };
        modelList = new String[8][];
        modelList[0] = new String[] {
            "Mustang",
            "F150",
            "Escape",
            "Fusion",
            "Explorer",
            "Bronco",
        };
        modelList[1] = new String[] {
            "Avalon",
            "Camry",
            "Prius",
            "Tacoma",
            "4Runner",
            "RAV4",
        };
        modelList[2] = new String[] {
            "Corvette",
            "Silverado",
            "Camaro",
            "Equinox",
            "Tahoe",
            "Blazer",
        };
        modelList[3] = new String[] {
            "Civic",
            "Accord",
            "CR-V",
            "Pilot",
            "Ridgeline",
            "HR-V",
        };
        modelList[4] = new String[] {
            "3 Series",
            "5 Series",
            "X5",
            "X3",
            "i8",
            "M3",
            "M5",
        };
        modelList[5] = new String[] {
            "Charger",
            "Challenger",
            "Durango",
            "Ram 1500",
            "Viper",
        };
        modelList[6] = new String[] {
            "Altima",
            "Sentra",
            "Maxima",
            "Rogue",
            "Frontier",
            "Titan",
        };
        modelList[7] = new String[] {
            "Wrangler",
            "Cherokee",
            "Grand Cherokee",
            "Gladiator",
            "Compass",
        };

        makeMenu = new JComboBox<String>(makeList);
        modelMenu = new JComboBox<String>(modelList[0]);

        makeMenu.setFont(appFontSmall);
        modelMenu.setFont(appFontSmall);

        carPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 12, 8));
        carPanel.add(new JLabel("Make:"));
        carPanel.add(makeMenu);
        carPanel.add(new JLabel("Model:"));
        carPanel.add(modelMenu);

        // options checkboxes
        option_stereo = new JCheckBox("Stereo Package", false);
        option_selfdrive = new JCheckBox("Automatic", false);
        option_eject = new JCheckBox("Ejection Seat", false);

        option_stereo.setFont(appFontSmall);
        option_selfdrive.setFont(appFontSmall);
        option_eject.setFont(appFontSmall);

        optionsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 12, 8));
        optionsPanel.add(option_stereo);
        optionsPanel.add(option_selfdrive);
        optionsPanel.add(option_eject);

        // color radio buttons
        color_red = new JRadioButton("Red");
        color_black = new JRadioButton("Black");
        color_silver = new JRadioButton("Silver");

        color_red.setFont(appFontSmall);
        color_black.setFont(appFontSmall);
        color_silver.setFont(appFontSmall);

        colorGroup = new ButtonGroup();
        colorGroup.add(color_red);
        colorGroup.add(color_black);
        colorGroup.add(color_silver);
        color_red.setSelected(true);

        colorPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 12, 8));
        colorPanel.add(color_red);
        colorPanel.add(color_black);
        colorPanel.add(color_silver);

        // transmission radio buttons
        trans_manual = new JRadioButton("Manual");
        trans_auto = new JRadioButton("Automatic");

        trans_manual.setFont(appFontSmall);
        trans_auto.setFont(appFontSmall);

        transPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 12, 8));
        transPanel.add(trans_manual);
        transPanel.add(trans_auto);

        transGroup = new ButtonGroup();
        transGroup.add(trans_manual);
        transGroup.add(trans_auto);
        trans_manual.setSelected(true);

        // build my car
        buildText = new JTextArea(14, 28);
        buildText.setFont(appFontSmall);
        buildText.setEditable(false);
        buildText.setLineWrap(true);
        buildText.setWrapStyleWord(true);
        buildText.setMargin(new Insets(10, 10, 10, 10));
        buildText.setText("Click the button to build your new car!");

        buildButton = new JButton("Build My Car!");
        buildButton.setFont(appFontSmall);

        JScrollPane buildScrollPane = new JScrollPane(buildText);
        buildScrollPane.setPreferredSize(new Dimension(340, 300));

        buildPanel.setLayout(new BorderLayout(0, 10));
        buildPanel.setBorder(BorderFactory.createTitledBorder("Build Summary"));
        JPanel upperBuild = new JPanel(new GridLayout(1, 1));
        JPanel lowerBuild = new JPanel(new FlowLayout(FlowLayout.CENTER));
        upperBuild.add(buildScrollPane);
        lowerBuild.add(buildButton);
        buildPanel.add(upperBuild, BorderLayout.CENTER);
        buildPanel.add(lowerBuild, BorderLayout.SOUTH);

        // center panel
        centerPanel.setLayout(new GridLayout(7, 1, 0, 8));
        centerPanel.setBorder(
            BorderFactory.createTitledBorder("Customize Your Car")
        );
        centerPanel.add(carPanel);

        JPanel optionsLabelPanel = new JPanel(
            new FlowLayout(FlowLayout.CENTER)
        );
        JLabel optionsLabel = new JLabel("OPTIONS:");
        optionsLabel.setFont(appFontSmall);
        optionsLabelPanel.add(optionsLabel);
        centerPanel.add(optionsLabelPanel);
        centerPanel.add(optionsPanel);

        JPanel colorLabelPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel colorLabel = new JLabel("COLOR:");
        colorLabel.setFont(appFontSmall);
        colorLabelPanel.add(colorLabel);
        centerPanel.add(colorLabelPanel);
        centerPanel.add(colorPanel);

        JPanel transLabelPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel transLabel = new JLabel("TRANSMISSION:");
        transLabel.setFont(appFontSmall);
        transLabelPanel.add(transLabel);
        centerPanel.add(transLabelPanel);
        centerPanel.add(transPanel);

        // add panels to main panel
        this.add(titlePanel, BorderLayout.NORTH);
        this.add(centerPanel, BorderLayout.CENTER);
        this.add(buildPanel, BorderLayout.EAST);

        // add action listeners
        makeMenu.addActionListener(this);
        modelMenu.addActionListener(this);
        color_red.addActionListener(this);
        color_black.addActionListener(this);
        color_silver.addActionListener(this);
        trans_manual.addActionListener(this);
        trans_auto.addActionListener(this);
        option_stereo.addActionListener(this);
        option_selfdrive.addActionListener(this);
        option_eject.addActionListener(this);
        buildButton.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == makeMenu) {
            int i = makeMenu.getSelectedIndex();
            modelMenu.removeAllItems();
            for (String model : modelList[i]) {
                modelMenu.addItem(model);
            }
            return;
        }

        if (e.getSource() == buildButton) {
            buildText.setText("");
            buildText.append("YOUR CUSTOM CAR\n");
            buildText.append("------------------------------\n");
            buildText.append(
                "Make: " + String.valueOf(makeMenu.getSelectedItem()) + "\n"
            );
            buildText.append(
                "Model: " + String.valueOf(modelMenu.getSelectedItem()) + "\n"
            );
            buildText.append("Color: " + getSelectedColor() + "\n");
            buildText.append(
                "Transmission: " + getSelectedTransmission() + "\n"
            );
            buildText.append("\nOptions:\n");

            if (
                option_stereo.isSelected() ||
                option_selfdrive.isSelected() ||
                option_eject.isSelected()
            ) {
                if (option_stereo.isSelected()) {
                    buildText.append("- Super Surround Stereo System\n");
                }
                if (option_selfdrive.isSelected()) {
                    buildText.append("- Automatic Drive System\n");
                }
                if (option_eject.isSelected()) {
                    buildText.append("- Passenger Ejection Seat\n");
                }
            } else {
                buildText.append("- No additional options selected\n");
            }
        }
    }

    private String getSelectedColor() {
        if (color_red.isSelected()) {
            return "Red";
        }
        if (color_black.isSelected()) {
            return "Black";
        }
        return "Silver";
    }

    private String getSelectedTransmission() {
        if (trans_manual.isSelected()) {
            return "Manual";
        }
        return "Automatic";
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Car Sales");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setContentPane(new CarSalesJPanel());
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
