package state;

/**
 * Created by Yonne on 05/12/13.
 */
public class Parameters {

    public int hidden_layers = 3;
    /**
     * input p, first 8 are sensory information (evenly distributed around bot)
     * 0:	sensor 1
     * 1:	sensor 2
     * 2:	sensor 3
     * 3:	sensor 4
     * 4:	sensor 5
     * 5:	sensor 6
     * 6:	sensor 7
     * 7:	sensor 8
     * 8:	unused
     * 9:   group number
     * 10:  left
     * 11:  right
     * 12:  down
     * 13:  up
     * 14:  split
     * 15:  merge
     */
    public int input_nodes = 16;	// 0 t/m 15 = 16 ;)
    /**
     * Output p:
     * 1: x-axis movement
     * 2: y-axis movement
     * 3: group number
     * 4: knows a command is given
     */
    public int output_nodes = 4;
    
    public int trainingCicles = 1000;

    public int agent_size = 24;
    public int agent_vision = 100;

    public int nr_of_agents = 4;
    public int n_sensors = 8;

    public boolean debug = true; // enables creation of debug-files
    public boolean log = true;  // enables creation of log-files

    public boolean start = false;
    public boolean first_run = true;
    public boolean trainingDone = false;

    public String log_dir = "./log/";
    public String debug_dir = log_dir + "debug/";

    // speed of the bot
    public int speed = 10;
    public int max_distance = 100;

    public String train_file = "./input-files/input4.txt";
}
