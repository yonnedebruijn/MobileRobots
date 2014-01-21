import state.Dimensions;
import state.Human;
import state.Parameters;
import state.World;
import view.ButtonPanel;
import view.ColorTheme;
import view.LoggingPanel;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

/**
 * Created by Yonne on 02/01/14.
 */
public class Main extends JFrame implements Runnable {
	/**
	 * 	@TODO:
	 * 	
	 *  Use the sensor-calculation.
	 * 		The sensors all have values of 0.00, so we should run the sensor-calculating function.
	 * 	
	 *  Write the report:
	 * 		Yeah, deadline is midnight of friday the 17th of january. It should be 5-10 pages,
	 * 		and we should have some interesting research questions etc etc. We should get started on this.
	 * 	
	 *  Test, test, test. Cleanup code, put copyright everywhere so the VU won't steal our beautiful stonercode ;) 
	 */

	// world information
	final Parameters p = new Parameters();
    final Dimensions d = new Dimensions(500, 500);
    
    // other used stuff.
    final long seed = 1234568;
    final Random random = new Random(seed);
    public ColorTheme ct = new ColorTheme();  // Contains the colors used for the groups/world    
    
    // logging panels, where we output our information 
    LoggingPanel log = new LoggingPanel("Log", 400, 300);
    LoggingPanel debug = new LoggingPanel("Debug", 400, 600);
    LoggingPanel inputs = new LoggingPanel("Inputs",400,200);

    // create the world and human (the human class gives the 'commands')
    World world = new World(p, d, ct, random, log, debug, true);
    final Human human = new Human(log,debug,world,p);
    
    // buttons used to, 'on the fly' change the settings
    ButtonPanel buttons = new ButtonPanel(world, p, log, debug);

    // gui generators
    final WorldViewer viewer = new WorldViewer(world);
    final JPanel split = new JPanel(new BorderLayout());


    public Main() {
    	// call the JFrame constructor
        super("ADS");
        
        // add all gui-related stuff.
        split.addKeyListener(human);
        split.setPreferredSize(new Dimension(d.get_width() + 835, d.get_height() + 200));
        split.add(log, BorderLayout.WEST);
        split.add(viewer, BorderLayout.CENTER);
        split.add(buttons, BorderLayout.SOUTH);
        split.add(debug, BorderLayout.EAST);
        split.add(inputs, BorderLayout.EAST);
        split.setFocusable(true);
        setContentPane(split);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
        split.requestFocus();
        log.addKeyListener(human);
        debug.addKeyListener(human);

    }

    public static void main(String[] args) {
    	System.out.println("start threads");
        try {
            // make java look like native application
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        } // it failed, so what...

        final Main m = new Main();
        new Thread(m).start();
    }


    @Override
    public void run() {
    	try{
        while (true) {
        	// do nothing while we haven't started
        	while(!p.start)
        	{
        		Thread.sleep(10);
        	}
        	// while we're started, do your thing.
            while (p.start) {
				split.requestFocus();
				world.step();

				// causes a refresh
				viewer.repaint();
				// sleep for repaint
				Thread.sleep(50);
           }
    	}
        } catch (InterruptedException e) {
    		e.printStackTrace();
    	}
    }
    
    /**
	 * 	Randomly generated serialversionUID since eclipse likes this.
	 */
	private static final long serialVersionUID = 4177070840031068075L;
}



