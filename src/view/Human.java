package view;

import state.Parameters;
import state.Watcher;

import javax.swing.*;
import java.awt.event.KeyEvent;

/**
 * Created by Yonne on 05/12/13.
 */

public class Human extends JFrame implements Observer {

    /**
	 * 	serialversionUID for making eclipse happy
	 */
	private static final long serialVersionUID = -8904438404845940013L;
	/**
     * Simulates the view.Human handing out commands to the bots
     * @todo Should contain the methods simulating the human (the actions)
     * @todo ActionListeners for the arrow-keys for movement
     * @todo 's' for split
     * @todo 'm' for merge
    * i.e. go left, go right, split, merge, e.t.c.
    * serves as input for the neural net of the bots (i.e. an extra input per action)
    */

    LoggingPanel log;
    LoggingPanel debug;
    Parameters p;


    public Human(Parameters p, LoggingPanel log, LoggingPanel debug) {
        Watcher watcher = new Watcher(log);
        watcher.add_observer(this);
        this.addKeyListener(watcher);
        this.log = log;
        this.debug = debug;
        this.p = p;
        // this.getContentPane().add(watcher); @todo keys not being registered
        setFocusable(true);
        requestFocus();
    }

    public void keyTyped(KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.VK_S) {
            debug.write("Split-key typed ('s')");
        }
        if (e.getKeyCode() == KeyEvent.VK_M) {
            debug.write("Merge-key typed ('m')");
        }

    }

    public void keyPressed(KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            debug.write("Right key pressed");
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            debug.write("Left key pressed");
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            debug.write("Down key pressed");
        }
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            debug.write("Up key pressed");
        }

    }

    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            debug.write("Right key released");
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            debug.write("Left key released");
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            debug.write("Down key released");
        }
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            debug.write("Up key released");
        }
    }

    @Override
    public void update(KeyEvent keyEvent,String input_form) {
        log.write(keyEvent.getKeyCode());
        if(input_form.equals("pressed"))
            keyPressed(keyEvent);
        else if (input_form.equals("released"))
            keyReleased(keyEvent);
        else if (input_form.equals("typed"))
            keyTyped(keyEvent);
    }
}

