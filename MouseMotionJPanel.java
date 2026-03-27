import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MouseMotionJPanel
    extends JPanel
    implements MouseMotionListener, MouseListener
{

    JLabel x_coord, y_coord;
    JButton leftButton, middleButton, rightButton;

    Color c;

    public MouseMotionJPanel() {
        setLayout(new BorderLayout());

        JPanel northPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        northPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        x_coord = new JLabel("X: ");
        y_coord = new JLabel("Y: ");
        northPanel.add(x_coord);
        northPanel.add(y_coord);

        leftButton = new JButton("Left");
        middleButton = new JButton("Middle");
        rightButton = new JButton("Right");

        southPanel.add(leftButton);
        southPanel.add(middleButton);
        southPanel.add(rightButton);

        add(northPanel, BorderLayout.NORTH);
        add(southPanel, BorderLayout.SOUTH);

        addMouseMotionListener(this);
        addMouseListener(this);
        leftButton.addMouseListener(this);
        leftButton.addMouseMotionListener(this);
        middleButton.addMouseListener(this);
        rightButton.addMouseListener(this);

        c = leftButton.getBackground();

        setVisible(true);
    }

    // MouseMotionListener
    @Override
    public void mouseDragged(MouseEvent e) {}

    @Override
    public void mouseMoved(MouseEvent e) {
        x_coord.setText("X: " + e.getX());
        y_coord.setText("Y: " + e.getY());
    }

    // MouseListener;
    public void mouseClicked(MouseEvent e) {}

    public void mouseEntered(MouseEvent e) {
        if (e.getSource() == leftButton) {
            leftButton.setBackground(Color.RED);
        }
        if (e.getSource() == middleButton) {
            middleButton.setBackground(Color.RED);
        }
        if (e.getSource() == rightButton) {
            rightButton.setBackground(Color.RED);
        }
    }

    public void mouseExited(MouseEvent e) {
        if (e.getSource() == leftButton) {
            leftButton.setBackground(c);
        }
        if (e.getSource() == middleButton) {
            middleButton.setBackground(c);
        }
        if (e.getSource() == rightButton) {
            rightButton.setBackground(c);
        }
    }

    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            leftButton.setBackground(Color.RED);
        }
        if (e.getButton() == MouseEvent.BUTTON2) {
            middleButton.setBackground(Color.RED);
        }
        if (e.getButton() == MouseEvent.BUTTON3) {
            rightButton.setBackground(Color.RED);
        }
    }

    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            leftButton.setBackground(c);
        }
        if (e.getButton() == MouseEvent.BUTTON2) {
            middleButton.setBackground(c);
        }
        if (e.getButton() == MouseEvent.BUTTON3) {
            rightButton.setBackground(c);
        }
    }
}
