import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GenericGUIApp extends JFrame {
    
    private static final int WIDTH = 400;
    private static final int HEIGHT = 400;
    
    public static void main(String[] args) {
        GenericGUIApp g = new GenericGUIApp();
        
    }
    
    public GenericGUIApp() {
        super("The Greatest Generic GUI App Ever");
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel jp = new MouseMotionJPanel();
        add(jp);
        setSize(WIDTH, HEIGHT);
        setResizable(false);
        setVisible(true);
        
    }
    
}