package view;

import state.Parameters;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Class used for logging
 * Panel contains a JTextArea for logging purposes
 */
// no serialversionuid for this. We don't always listen to eclipse.
@SuppressWarnings("serial")
public class LoggingPanel extends JPanel {

    final JLabel title_label;
    JTextPane debug;
    JScrollPane scp;
    JLabel run_time;
    Style style;

    /**
     * @param title Name
     */
    public LoggingPanel(String title, int width, int height) {
        super();

        setPreferredSize(new Dimension(width, height));
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
        run_time = new JLabel();
        run_time.setText("Simulation Time: --");
        title_label = new JLabel(title);
        debug = new JTextPane();
        style = debug.addStyle("Color", null);
        scp = new JScrollPane(debug);
        add(run_time);
        add(title_label);
        add(scp);


    }

    void update() {
        // TODO enable again
//        iterations.setText("Iterations: " + Double.toString(sim.world.iteration) + " - " + sim.world.epochIterations);
//        trails.setText("Trials: " + Integer.toString(sim.world.trials + 1) + " - " + sim.world.epoch);
//        generations.setText("Generations: " + Integer.toString(sim.world.currentGen) + " - " + sim.world.generations);
    }

    /**
     * Writes a String to the debug window
     * in a specific color
     *
     * @param msg
     */
    public void write(String msg, Color color) {
        append(now(msg), color);
    }

    /**
     * Writes a Double to the debug window
     * in a specific color
     *
     * @param msg
     */
    public void write(double msg, Color color) {
        append(now(Double.toString(msg)), color);

    }

    /**
     * Writes a String to the debug window
     *
     * @param msg
     */
    public void write(String msg) {
        append(now(msg));
    }

    /**
     * Writes a Double to the debug window
     *
     * @param msg
     */
    public void write(double msg) {
        append(now(Double.toString(msg)));
    }

    /**
     * Writes the parameters to the debug-window
     *
     * @param p Parameter-object containing the required params
     */
    public void write_parameters(Parameters p) {
        append(now() + "\n");
        append("--Neural Net--");
        append("Input Nodes: " + p.input_nodes);
        append("Hidden Nodes: " + p.hidden_layers);
        append("Output Nodes: " + p.output_nodes + "\n");
        append("--Agent Parameters--");
        append("Number of Agents: " + p.nr_of_agents);
        append("Number of Sensors: " + p.n_sensors + "\n");
    }

    /**
     * Writes the debug window to a date/time-stamped debug.txt file
     * If dir is missing, creates dir and tries again
     */
    public void write_file(Parameters p, double running_time) {
        if (!p.log) {
            write_error("No Files Created: Logging Disabled");
            return;
        }

        write("Simulation Time: " + running_time + " sec");
        String name = p.log_dir + now() + "-log.log";
        try {
            PrintWriter out = new PrintWriter(name);
            out.print(debug.getText());
            out.close();
            write("Logfile created: " + name, Color.green);
            return;
        } catch (FileNotFoundException e) {
            write_error("File Writing Error: " + e.getMessage());
            File dir = new File(p.log_dir);
            dir.mkdir();
            write("Directory created: " + p.log_dir, Color.green);
        }

        try {
            PrintWriter out = new PrintWriter(name);
            out.print(debug.getText());
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        write("Logfile created: " + name, Color.green);
    }

    public void write_debug(Parameters p) {
        if (!p.debug) {
            write_error("No Files Created: Debugging Disabled");
            return;
        }
        String name = p.debug_dir + now() + "-debug.log";
        try {
            PrintWriter out = new PrintWriter(name);
            out.print(debug.getText());
            out.close();
            write("Debugfile created: " + name, Color.green);
            return;
        } catch (FileNotFoundException e) {
            write_error("File Writing Error: " + e.getMessage());
            File dir = new File(p.debug_dir);
            dir.mkdir();
            write("Directory created: " + p.debug_dir, Color.green);
        }

        try {
            PrintWriter out = new PrintWriter(name);
            out.print(debug.getText());
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        write("Debugfile created: " + name, Color.green);
    }


    /**
     * Adds timestamp to provided message (for logging purposes)
     *
     * @param msg
     * @return
     */
    public static String now(String msg) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh.mm.ss");
        return sdf.format(cal.getTime()) + ":   " + msg;
    }

    /**
     * Returns the time
     *
     * @return
     */
    public static String now() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh.mm.ss");
        return sdf.format(cal.getTime());
    }

    public void clear() {
        debug.setText("");
    }

    /**
     * Known bugs printed to the debug window
     */
    public void known_bugs() {
        append("KNOWN BUGS", Color.red);
        append("NONE", Color.green);
    }

    public void write_error(String msg) {
        append(now(msg), Color.red);
    }

    /**
     * Append text with a different color
     *
     * @param s     msg to append
     * @param color color of the msg
     */
    public void append(String s, Color color) {
        StyleConstants.setForeground(style, color);
        try {
            Document doc = debug.getStyledDocument();
            doc.insertString(doc.getLength(), s + "\n", style);
        } catch (BadLocationException exc) {
            exc.printStackTrace();
        }
    }

    public void append(String s) {
        StyleConstants.setForeground(style, Color.black);
        try {
            Document doc = debug.getStyledDocument();
            doc.insertString(doc.getLength(), s + "\n", style);
        } catch (BadLocationException exc) {
            exc.printStackTrace();
        }
    }

    public void update_runningtime(double running_time) {
        Date runtime = new Date((long) running_time / 1000);
        run_time.setText("Simulation Time: " + runtime.getTime() + " sec");
    }

    public void set_text(String s, Color color)
    {
        debug.setText(s);
    }

}