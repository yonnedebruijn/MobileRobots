package state;

import view.LoggingPanel;

import java.util.ArrayList;
import java.util.Calendar;

import cern.colt.Arrays;

/**
 * Created by Yonne on 02/01/14.
 */
public class Agent {

    ArrayList<Coordinate> positions = new ArrayList<Coordinate>();
    ArrayList<Double> wall_distance = new ArrayList<Double>();
    ArrayList<Double> agent_distance = new ArrayList<Double>();
    Agent captain = null;
    double[] input;
    Parameters p;
    Controller c;
    double direction;
    int group_id;
    LoggingPanel log;
    LoggingPanel debug;
    Jungle j;
    Group g;
	int iterations = 0;


    public Agent(Coordinate start_cord, Parameters p, int group_id, Controller c, LoggingPanel log, LoggingPanel debug) {
        positions.add(start_cord);
        this.log = log;
        this.debug = debug;
        this.p = p;
        this.c = c;
        this.group_id = group_id;
        this.direction = 0; // robot starts facing to the north
        input = new double[p.input_nodes];
        for(int i = 0; i < p.input_nodes; i++)
        {
            input[i] = 0;
        }
        input[9] = 1;
    }
    
    public void setGroup(Group g)
    {
    	this.g = g;
    }
    
    public void setCaptain(Agent cappie)
    {
    	this.captain = cappie;
    }
    
    public void setJungle(Jungle j)
    {
    	this.j = j;
    }

    /**
     * Move agent with certain {@code speed} in the direction {@code dx, dy}
     *
     * @param dx         Move this much in x direction (+ left / - right)
     * @param dy         Move this much in y direction (+ down / - up)
     * @param boundaries
     */
    public void move(double dx, double dy, Dimensions boundaries) {
        final Coordinate old = get_pos();
        int count = 0;
        Coordinate c = new Coordinate(dy, dx);
        // if we're not the captain, and our next move results in a collision
        // we do something else.
        while(captain != null && collision(g.agents, c) > 0 && count < 10)
        {
        	double r1 = Math.random();
        	double r2 = Math.random();
        	if(r1 > 0.5) dx = -dx;
        	if(r2 > 0.5) dy = -dy;
        	c = new Coordinate(dx, dy);
        	count++;
        }
        c.scale(p.speed);
        c.add(old);
        c.bound(p.agent_size, boundaries);
        positions.add(c);
    }


    /**
     * Sets the ArrayList wall_distance with the relative distance to the walls
     *
     * @param relative_distance an array with n relative distances to the wall in doubles
     */
    public void set_wall_distance(ArrayList<Double> relative_distance) {
        this.wall_distance = relative_distance;
    }

    /**
     * Sets the ArrayList agent_distance with the relative distance to the other agents
     *
     * @param relative_distance an array with n relative distances to the wall in doubles
     */
    public void set_agent_distance(ArrayList<Double> relative_distance) {
        this.agent_distance = relative_distance;
    }

    public ArrayList<Double> get_wall_array() {
        return this.wall_distance;
    }

    public ArrayList<Double> get_agents_array() {
        return this.agent_distance;
    }

    /**
     * Returns the relative distance from
     * this agent to another one
     *
     * @param a
     * @return
     */
    public double distance(Agent a) {
        if (a == null)
            return 0d;

        return get_pos().distance(a.get_pos());
    }

    /**
     * @return Coordinate-object containing current position
     */
    public Coordinate get_pos() {
        return positions.get(positions.size() - 1);
    }

    /**
     * @return Parameter-object
     */
    public Parameters get_params() {
        return this.p;
    }

    /**
     * @return Group number
     */
    public int get_group() {
        return this.group_id;
    }

    public void addController(Controller c) {
        this.c = c;
    }

    public Controller getController() {
        return this.c;
    }
    
    public void clearSensors()
    {
    	for(int i = 0; i < p.n_sensors; i++)
    	{
    		input[i] = 0;
    	}
    }
    
    public void findCaptain()
    {
    	//set group id to true
    	input[9] = 1;
    	// do we have a captain????
    	if(captain == null) return;
    	// reset the sensory input.
    	
    	int cap_x = (int) captain.get_pos().get_x();
    	int cap_y = (int) captain.get_pos().get_y();
    	
    	int dx = cap_x - (int) get_pos().get_x();
    	int dy = cap_y - (int) get_pos().get_y();
    	
    	// check which sensor(s) to activate
    	/**
    	 * sensor 0: above
    	 * sensor 1: right above
    	 * sensor 2: right
    	 * sensor 3: right below
    	 * sensor 4: below
    	 * sensor 5: left below
    	 * sensor 6: left
    	 * sensor 7: left above
    	 */
    	// if we are too close, we're done.
    	if((dx < 40 && dy < 40) && (dx > -40 && dy > -40)) return;
    	
    	// if the x axis is too close, only move on the y-axis
    	if(dx < 40 && dx > -40)
    	{
    		// is he above or below us? above:
    		if(dy < 0){ input[0] = 1; return;}
    		// below:
    		input[4] = 1; return;
    	}
    	// if the y axis is too close, only move on the x-axis
    	if(dy < 40 && dy > -40)
    	{
    		// is he left or right? right:
    		if(dx > 0){ input[2] = 1; return;}
    		// left:
    		input[6] = 1; return;
    	}
    	// he is somewhere left above, right above, left below or right below...
    	// somewhere above
    	if(dy < 0)
    	{
    		// to the right 
    		if(dx > 0) { input[1] = 1; return; }
    		// to the left
    		input[7] = 1; return;
    	}
    	// he is below us... left or right?
    	if(dx > 0) { input[3] = 1; return; }
    	input[5] = 1; return;
    }

    /**
     * Let the agent take a step.
     *
     * @param boundaries Dimension-object containing boundaries
     */
    public void step(Dimensions boundaries, ArrayList<Agent> l) {
    	clearSensors();
    	findCaptain();
        NetworkResults result = c.generateMove(input);
        iterations--;
        if(result.group_id == 0 && captain != null && iterations < -20)
        {
        	// randomly decide if to join another group, if we're not the captain
        	System.out.println("Maybe joining another group.");
        	double r = Math.random();
        	if(r > 0.5){
        		System.out.println("new group for this agent!");
        		// remove this group, add to another
        		j.remove_agent(group_id, this);
        	}
        	iterations = 0;
        }
        if(input[15] == 1 && iterations < -20)
        {
        	j.addToOtherGroup(this);
        	iterations = 0;
        }
        move(result.x_axis, result.y_axis, boundaries);
    }

    /**
     * @param agents List containing agents to compare to
     * @return number of collissions made (0 if none)
     */
    public int collision(ArrayList<Agent> agents, Coordinate c) {
    	int result = 0 ;
    	// the coordinate is the bottom right position of the agent's circle, so let's use
    	// that to find the center of the circle
    	double br_x = c.get_x() + positions.get(positions.size()-1).get_x();
        double br_y = c.get_y()+ positions.get(positions.size()-1).get_y();
        double center_x =  br_x - p.agent_size;
        double center_y = br_y - p.agent_size;
        
        // now loop through all other agents
        for(Agent a : agents){
        	if(a != this && a != captain){
		        double target_br_x = a.get_pos().get_x();
		        double target_br_y = a.get_pos().get_y();
		        double target_x = target_br_x - p.agent_size;
		        double target_y = target_br_y - p.agent_size;
		        double radius = p.agent_size + 10;
		
		        if (Math.abs(center_x - target_x) < radius && Math.abs(center_y - target_y) < radius) {
		            result++;
		        }
        	}
        }
        return result;
    }

    /**
     * Set a specific input value
     * @param value
     * @param index
     */
    public void set_specific_input(double value, int index)
    {
        input[index] = value;
    }


    public void set_group_input(int group_nr)
    {
        input[9] = 1;
    }

    /**
     * Sets the specific input to 1.0, reverting the rest to 0.0
     * @param index input to be change.
     * 10: left
     * 11: right
     * 12: down
     * 13: up
     * 14: split
     * 15: merge
     * 16: clear
     */
    public void set_human_input(int index)
    {
        reset_human_input();
        if(index == 16)
            return;

        input[index] = 1.0;
        }


    public void reset_human_input()
    {
        int upscale = 10;
        for(int i = 0; i < 6; i++)
        {
            input[i+upscale] = 0.0;
        }
    }

}
