import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;



public class Frame extends JFrame{
    private final Dimension screen = new Dimension(500,500);
    
    public Frame(){
        
       
        
        init();
    }
    private void init(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(screen);
        setVisible(true);
        setResizable(false);
        setFocusable(true);
        
        Screen s = new Screen();
        addMouseMotionListener(s.key);
        addMouseListener(s.key);
        add(s);
        setLocationRelativeTo(null);
        pack();
    }
    
    public static void main(String args[]){
        Frame f = new Frame();
        
    }
}
