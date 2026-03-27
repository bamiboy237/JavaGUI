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
        this.setLayout(new BorderLayout());

        JPanel titlePanel = new JPanel();
        JPanel carPanel = new JPanel();
        JPanel optionsPanel = new JPanel();
        JPanel colorPanel = new JPanel();
        JPanel transPanel = new JPanel();
        JPanel buildPanel = new JPanel();

        JPanel centerPanel = new JPanel();

        Font appFontLarge = new Font("Georgia", Font.PLAIN, 24);
        Font appFontSmall = new Font("Georgia", Font.PLAIN, 16);

        // title
        JLabel titleLabel = new JLabel("Car Sales");
        titleLabel.setFont(appFontLarge);
        titlePanel.add(titleLabel);
        
    }
}
