package state;

import view.LoggingPanel;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Calendar;

/**
 * Created by Yonne on 05/12/13.
 */

public class Human implements KeyListener {

    /**
     * Simulates the state.Human handing out commands to the bots
    * i.e. go left, go right, split, merge, e.t.c.
    * serves as input for the neural net of the bots (i.e. an extra input per action)
    * index-numbers used:
    * 10: left
    * 11: right
    * 12: down
    * 13: up
    * 14: split
    * 15: merge
    * 16: clear
    */
    Parameters p;
    World world;
    LoggingPanel debug;
    LoggingPanel log;
    int left 	= 10;
    int right 	= 11;
    int down 	= 12;
    int up 		= 13;
    int split 	= 14;
    int merge 	= 15;
    int clear 	= 16;

    
    int group = 1;
    
    
    public Human(LoggingPanel log, LoggingPanel debug, World world, Parameters p) {
    	System.out.println("human created");
        this.p = p;
        this.world = world;
        this.debug = debug;
        this.log = log;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            debug.write("Right key pressed");
            world.set_human_input(right, group);
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            debug.write("Left key pressed");
            world.set_human_input(left, group);
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            debug.write("Down key pressed");
            world.set_human_input(down, group);
        }
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            debug.write("Up key pressed");
            world.set_human_input(up, group);
        }
        if (e.getKeyCode() == KeyEvent.VK_S) {
            debug.write("Split-key pressed ('s')");
            world.set_human_input(split, group);
        }
        if (e.getKeyCode() == KeyEvent.VK_M) {
            debug.write("Merge-key pressed ('m')");
            world.set_human_input(merge, group);
        }
        if (e.getKeyCode() == KeyEvent.VK_C) {
        	debug.write("Clear-key pressed ('c')");
        	world.set_human_input(clear, group);
        }
        if(e.getKeyCode() == KeyEvent.VK_1) {
        	debug.write("selected group 1");
        	group = 1;
        }
        if(e.getKeyCode() == KeyEvent.VK_2) {
        	debug.write("selected group 2");
        	group = 2;
        }
		if(e.getKeyCode() == KeyEvent.VK_3) {
			debug.write("selected group 3");
		    group = 3;    	
		}
		if(e.getKeyCode() == KeyEvent.VK_4) {
			debug.write("selected group 4");
			group = 4;
		}
		if(e.getKeyCode() == KeyEvent.VK_5) {
			debug.write("selected group 5");
			group = 5;
		}

    }
    @Override
    public void keyReleased(KeyEvent e) {
        world.set_human_input(clear, group);
    }
}

