import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GenericGUIApp extends JFrame {
    
    private static final int WIDTH = 1000;
    private static final int HEIGHT = 700;
    
    public static void main(String[] args) {
        GenericGUIApp g = new GenericGUIApp();
        
    }
    
    public GenericGUIApp() {
        super("The Greatest Generic GUI App Ever");
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel jp = new BGR_WikiRabbitHole();
        add(jp);
        setSize(WIDTH, HEIGHT);
        setResizable(false);
        setVisible(true);
        
    }
    
}