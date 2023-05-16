package Game;

import Object.GameObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * Screen for the Game
 * Mouse input
 */
public class GameView extends JFrame implements OnGameView {

    public final static int SCREEN_WIDTH = 800;
    public final static int SCREEN_HEIGHT = 800;

    public final static int WIDTH_BTN_MENU = 100;
    public final static int HEIGHT_BTN_MENU = 100;

    private ArrayList<ActionListener> on_resume = new ArrayList<>();
    private ArrayList<ActionListener> on_start = new ArrayList<>();
    private ArrayList<ActionListener> on_reset = new ArrayList<>();

    private JButton resume_btn;
    private JButton start_btn;
    private JButton quit_btn;

    private Point mouse_start = new Point();


    public GameView() {
        super();
        setUndecorated(true);
        setLayout(null);

        setSize(SCREEN_WIDTH, SCREEN_HEIGHT);


        this.resume_btn = create_button(0, "Resume", e -> {
            for(ActionListener s: on_resume){
                s.actionPerformed(e);
            }
            menu_toggle(false);
        });


        this.start_btn = create_button(0, "Start", e -> {
            for(ActionListener s: on_start){
                s.actionPerformed(e);
            }
            menu_toggle(false);
            start_btn.setVisible(false);

        });

       this.quit_btn = create_button(1, "Quit", e -> {
            this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        });;


        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                mouse_start.x = e.getX();
                mouse_start.y = e.getY();
            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        this.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                setLocation(e.getXOnScreen() - mouse_start.x, e.getYOnScreen() - mouse_start.y);
                System.out.println(e);
            }

        });



        quit_btn.setVisible(true);
        start_btn.setVisible(true);
        resume_btn.setVisible(false);

        setLocationRelativeTo(null);
        setVisible(true);
        setFocusable(true);
    }
    private void reset_frame(){
        getContentPane().removeAll();
        add(quit_btn);
        add(resume_btn);

    }

    /**
     * Add the game objects to the game view
     * @param gameObjects all the objects for the map
     */

    public void add_objects(ArrayList<GameObject> gameObjects) {
        Point p = new Point(0,0);
        reset_frame();
        for (int i = 0; i < gameObjects.size(); i++) {
            add(gameObjects.get(i));
            gameObjects.get(i).update(new Point(0,0));
        }

        repaint();

    }

    /**
     * method for creating a menu button
     */
    private JButton create_button(int column_index, String txt, ActionListener l){
        JButton btn = new JButton();
        btn.setLayout(null);
        btn.setText(txt);
        btn.setFont(new Font("Arial", Font.BOLD, 20));
        btn.setBackground(Color.gray);
        btn.setBounds(SCREEN_WIDTH/2 - WIDTH_BTN_MENU/2, SCREEN_HEIGHT/2 - HEIGHT_BTN_MENU/2 + HEIGHT_BTN_MENU*column_index, WIDTH_BTN_MENU, HEIGHT_BTN_MENU);
        btn.addActionListener(l);

        add(btn);
        return btn;
    }


    /**
     * method for creating a menu button
     */
    private void menu_toggle(boolean toggle){
        this.quit_btn.setVisible(toggle);
        this.resume_btn.setVisible(toggle);
    }
    /**
     * for on start button press
     */
    @Override
    public void on_start_menu_add(ActionListener l) {
       on_start.add(l);
    }

    /**
     * on resume button press
     */
    @Override
    public void on_resume_menu_add(ActionListener l) {
        on_resume.add(l);
    }



    public void open_menu_esc(){
        menu_toggle(true);
    }


}