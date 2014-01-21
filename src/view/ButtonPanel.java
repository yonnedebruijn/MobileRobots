package view;

import state.Parameters;
import state.World;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

/**
 * Contains the buttons and corresponding listeners
 * Created by Yonne on 07/01/14.
 */
public class ButtonPanel extends JPanel {
    /**
	 * 	serialVersionUID since eclipse loves it.
	 */
	private static final long serialVersionUID = 358299536642873072L;
	JButton reset = new JButton("Reset");
    JButton start = new JButton("Start/Continue");
    JButton pause = new JButton("Pause");
    JButton write_log = new JButton("Write Log");
    JButton stop = new JButton("Stop");
    JButton open_folder = new JButton("Open Log Folder");

    World world;
    Parameters p;
    JTextField hidden = new JTextField("", 20);
    JTextField nr_of_agents = new JTextField("", 20);
    JLabel hidden_label = new JLabel("Nr of Hidden Nodes");
    JLabel agents_label = new JLabel("Nr of Agents");
    LoggingPanel log;
    LoggingPanel debug;

    public ButtonPanel(World world, Parameters p, LoggingPanel log, LoggingPanel debug) {
        super();
        this.log = log;
        this.debug = debug;
        this.world = world;
        this.p = p;
        setLayout(new GridLayout());
        setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
        hidden.setText(Integer.toString(p.hidden_layers));
        nr_of_agents.setText(Integer.toString(p.nr_of_agents));

        add(start);
        add(pause);
        add(stop);
        add(reset);
        add(write_log);
        add(hidden);
        add(hidden_label);
        add(nr_of_agents);
        add(agents_label);
        add(open_folder);

        stop.addActionListener(new ButtonListener());
        write_log.addActionListener(new ButtonListener());
        reset.addActionListener(new ButtonListener());
        start.addActionListener(new ButtonListener());
        pause.addActionListener(new ButtonListener());
        open_folder.addActionListener(new ButtonListener());

    }

    class ButtonListener implements ActionListener {
        ButtonListener() {
        }

        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equals("Reset")) {
                if (p.start) {
                    log.write("Stop the Simulation first", Color.red);
                } else {
                    p.nr_of_agents = Integer.parseInt(nr_of_agents.getText());
                    p.hidden_layers = Integer.parseInt(hidden.getText());
                    world.reset();
                }
            } else if (e.getActionCommand().equals("Start/Continue")) {
                if(p.start){
                    log.write("Simulation already started",Color.red);
                    return;
                }
                world.start_time();
                log.write("Simulation Started", Color.green);
                p.nr_of_agents = Integer.parseInt(nr_of_agents.getText());
                p.hidden_layers = Integer.parseInt(hidden.getText());
                p.start = true;
            } else if (e.getActionCommand().equals("Pause")) {
                world.stop_time();
                log.write("Simulation Paused", Color.red);
                p.start = false;
            } else if (e.getActionCommand().equals("Write Log")) {
                log.write_file(p, world.get_running_time() / 1000);
                debug.write_debug(p);

            } else if (e.getActionCommand().equals("Stop")) {
                if (p.start) {
                    p.start = false;
                    log.write_file(p, world.get_running_time() / 1000);
                    debug.write_debug(p);
                } else {
                    log.write("Simulation not running", Color.red);
                }
            } else if (e.getActionCommand().equals("Open Log Folder")) {
                try {
                    Desktop.getDesktop().open(new File(p.log_dir));
                    return;
                } catch (IOException | IllegalArgumentException e1) {
                    log.write_error("Directory Error: " + e1.getMessage());
                    File dir = new File(p.log_dir);
                    dir.mkdir();
                    log.write("Directory created: " + p.log_dir, Color.green);
                }
                try {
                    Desktop.getDesktop().open(new File(p.log_dir));
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }

    }
}
