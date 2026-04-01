import java.awt.event.*;
import javax.swing.*;

public class MenuDemo extends JFrame implements ActionListener {

    private JMenuItem fileMenu_open;
    private JMenuItem fileMenu_save;
    private JMenuItem fileMenu_exit;

    JComboBox<String> personMenu;
    JComboBox<String> personOperationMenu;
    String[] people;
    String[] personDetails;
    String[] personOperations;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MenuDemo());
    }

    public MenuDemo() {
        super("Menu Example");
        setSize(300, 300);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        addWindowListener(
            new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    exitMenuDemo();
                }
            }
        );

        JMenuBar bar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);

        fileMenu_open = new JMenuItem("Open");
        fileMenu_open.addActionListener(this);
        fileMenu_open.setMnemonic(KeyEvent.VK_O);

        fileMenu_save = new JMenuItem("Save");
        fileMenu_save.addActionListener(this);
        fileMenu_save.setMnemonic(KeyEvent.VK_S);

        fileMenu_exit = new JMenuItem("Exit");
        fileMenu_exit.addActionListener(this);
        fileMenu_exit.setMnemonic(KeyEvent.VK_X);

        fileMenu.add(fileMenu_open);
        fileMenu.add(fileMenu_save);
        fileMenu.addSeparator();
        fileMenu.add(fileMenu_exit);

        bar.add(fileMenu);

        JMenu helpMenu = new JMenu("Help");
        helpMenu.setMnemonic(KeyEvent.VK_H);
        bar.add(Box.createHorizontalGlue());
        bar.add(helpMenu);

        setJMenuBar(bar);
        setLocationRelativeTo(null);
        setVisible(true);

        // title Panel
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("Select a Person:");
        titlePanel.add(titleLabel);

        // Person Selection
        people = new String[] {
            "George Washington",
            "Abraham Lincoln",
            "Grover Cleveland",
            "Harry Truman",
            "James Carter",
            "Ronald Reagan",
        };
        personDetails = new String[] {
            "Class: Person | First Name: George | Last Name: Washington",
            "Class: Person | First Name: Abraham | Last Name: Lincoln",
            "Class: RegisteredPerson | First Name: Grover | Last Name: Cleveland | Government ID: GP-102938",
            "Class: RegisteredPerson | First Name: Harry | Last Name: Truman | Government ID: GP-564738",
            "Class: OCCCPerson | First Name: James | Last Name: Carter | Government ID: GP-908172 | Student ID: OCCC-445566",
            "Class: OCCCPerson | First Name: ROnald | Last Name: Reagan | Government ID: GP-221144 | Student ID: OCCC-778899",
        };
        // Person Operations
        personOperations = new String[] {
            "View Details",
            "Edit Information",
            "Delete Person",
        };

        // Drop Down Selection
        personMenu = new JComboBox<String>(people);
        personOperationMenu = new JComboBox<String>(personOperations);
    }

    private void exitMenuDemo() {
        int result = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to exit?",
            "Exit Menu Demo",
            JOptionPane.YES_NO_OPTION
        );
        if (result == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == fileMenu_open) {
            System.out.println("OPEN WAS SELECTED");
        }
        if (e.getSource() == fileMenu_save) {
            System.out.println("SAVE WAS SELECTED");
        }
        if (e.getSource() == fileMenu_exit) {
            exitMenuDemo();
        }
    }
}
