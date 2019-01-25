
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.Timer;
import javax.swing.event.MouseInputAdapter;

public class Screen extends JPanel implements Runnable,ActionListener{
    Key key;
    private final String colors[] = {"red", "green", "yellow"};
    private JComboBox colorsList;
    private boolean running;
    private String selectedColor = "red";
    private JButton redo, undo;
    private ArrayList<int[]> cord;
    private ArrayList<int[]> lredo;
    private int id = 0;
    private int redoid = 0;
    private final Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
    private final int WIDTH = 1000;
    private final int HEIGHT = 500;
    private Timer time;
    
    public Screen(){
        super(new BorderLayout());
        this.cord = new ArrayList<>();
        this.lredo = new ArrayList<>();
        key = new Key();
        running = true;
        
        init();
    }
    
    private void init(){
        setFocusable(true);
        setLayout(null);
        setPreferredSize(new Dimension(WIDTH,HEIGHT));
        
        addMouseMotionListener(key);
        addMouseListener(key);
        
        undo = new JButton();
        undo.addActionListener(this);
        undo.setText("Undo");
        undo.setBounds(0,10,70,20);
        add(undo);
        
        colorsList = new JComboBox(colors);
        colorsList.setSelectedIndex(0);
        colorsList.addActionListener(this);
        colorsList.addMouseListener(key);
        colorsList.setBounds(140, 10, 70, 20);
        add(colorsList);
        
        
        redo = new JButton();
        redo.addActionListener(this);
        redo.setText("Redo");
        redo.setBounds(70,10,70,20);
        add(redo);

        
    }
    
    private void clearScreen(){
        Graphics g = getGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(0, 50, WIDTH, HEIGHT);
        
        this.cord = new ArrayList<>();
        this.lredo = new ArrayList<>();
    }
    
    @Override
    public void paint(Graphics g){
        super.paint(g);
        g.setColor(Color.BLACK);
        g.fillRect(0, 50, WIDTH, HEIGHT);
    }
    
    @Override
    public void run(){
        while(running){
            
        }
    }
    
    
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == colorsList){
            JComboBox cb = (JComboBox)e.getSource();
            selectedColor = (String)cb.getSelectedItem();
        }
        else if(e.getSource() == redo){
            redo();
        }else if(e.getSource() == undo){
            undo();
        }
        
        
    }
    private void redo(){
        Graphics g = getGraphics();
        
        if(lredo.size()>0){
            g.setColor(Color.black);
            g.fillRect(0, 50, WIDTH, HEIGHT);
            for(int i = 0; i<cord.size(); i++){
                switch (cord.get(i)[2]) {
                    case 0:
                        g.setColor(Color.red);
                        break;
                    case 1:
                        g.setColor(Color.GREEN);
                        break;
                    default:
                        g.setColor(Color.YELLOW);
                        break;
                }
                g.fillOval(cord.get(i)[0], cord.get(i)[1], 10, 10);
            }
            int temp = lredo.get(lredo.size()-1)[3];
            for(int i = 0; i<lredo.size(); i++){
                if(lredo.get(i)[3] == temp){
                    switch (lredo.get(i)[2]) {
                    case 0:
                        g.setColor(Color.red);
                        break;
                    case 1:
                        g.setColor(Color.GREEN);
                        break;
                    default:
                        g.setColor(Color.YELLOW);
                        break;
                }
                g.fillOval(lredo.get(i)[0], lredo.get(i)[1], 10, 10);
                cord.add(lredo.get(i));
                lredo.remove(i);
                i--;
                }
                
            }
            id++;
        }
    }
    
    private void undo(){
        Graphics g = getGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(0, 50, WIDTH, HEIGHT);
        g.setColor(Color.red);
        boolean deleted = false;
        int tempid = id;
        for(int i = 0; i<cord.size(); i++){
            if(cord.get(i)[3] != id){
                switch (cord.get(i)[2]) {
                    case 0:
                        g.setColor(Color.red);
                        break;
                    case 1:
                        g.setColor(Color.GREEN);
                        break;
                    default:
                        g.setColor(Color.YELLOW);
                        break;
                }
                
                
                g.fillOval(cord.get(i)[0], cord.get(i)[1], 10, 10);
            }
            else{
                lredo.add(cord.get(i));
                cord.remove(i);
                i--;
                if(!deleted && id>=0){
                    tempid--;
                    deleted = true;
                }
                    
            }
            
        }
        
        id = tempid;
        if(deleted){
            redoid++;
        }
    }
    
    private class Key extends MouseInputAdapter{

        @Override
        public void mouseDragged(MouseEvent e) {
            
            Graphics g;
            g = getGraphics();
            int col = colorsList.getSelectedIndex();
            
            switch (col) {
            case 0:
                g.setColor(Color.red);
                break;
            case 1:
                g.setColor(Color.GREEN);
                break;
            default:
                g.setColor(Color.YELLOW);
                break;
            }
            if(e.getY()>50){
                g.fillOval(e.getX(), e.getY(), 10, 10);
                int[] cor = new int[4];
                cor[0] = e.getX();
                cor[1] = e.getY();
                cor[2] = col;
                cor[3] = id;
                cord.add(cor);
                lredo = new ArrayList<>();
                redoid = 0;
            }
            
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            
        }
        
        @Override
        public void mouseClicked(MouseEvent e){
            
            Rectangle r = colorsList.getBounds();
            if(colorsList.getMousePosition().getX() == e.getX() && colorsList.getMousePosition().getY() == e.getY()){
                undo();
                redo();
                System.out.println("called");
            }
            else{
                System.out.println("Mouse position: " + e.getX() + " " + e.getY());
                System.out.println(colorsList.getMousePosition().toString());
            }
        }
        
        @Override
        public void mousePressed(MouseEvent e){
            
            System.out.println("Click initialized. Current id = " + id);
            if(e.getY() > 50){
                id++;
            }
            System.out.println("After id = " + id);
        }
           
    }
    
}
