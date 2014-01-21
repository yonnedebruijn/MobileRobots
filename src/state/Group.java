package state;

import view.LoggingPanel;

import java.awt.Color;
import java.util.ArrayList;

/**
 * Class that holds ArrayList<Agent> containing the
 * Agent-objects
 * Created by Yonne on 05/12/13.
 * First group_id starts at 0 (not 1)
 */
public class Group {

    ArrayList<Agent> agents = new ArrayList<Agent>();
    int group_id;
    LoggingPanel debug;
    LoggingPanel log;
    Jungle j;

    public Group(LoggingPanel debug, LoggingPanel log, int group_id, Jungle j) {
    	this.j = j;
        this.debug = debug;
        this.log = log;
        this.group_id = group_id;
    }

    public Group(int group, ArrayList<Agent> agents,LoggingPanel debug,LoggingPanel log, Jungle j) {
    	this.j = j;
        this.debug = debug;
        this.log = log;
        this.group_id = group;
        this.agents = agents;
        if(agents.size() != 0){
        	agents.get(0).setCaptain(null);
        	agents.get(0).clearSensors(); 
        	agents.get(0).group_id = group; 
        	agents.get(0).setJungle(j);
        	agents.get(0).setGroup(this);}
        for(int i = 1; i < agents.size(); i++)
        {
        	agents.get(i).setCaptain(agents.get(0));
        	agents.get(i).setJungle(j);
        	agents.get(i).group_id = group;
        	agents.get(i).setGroup(this);
        }
    }
    

    public boolean contains(Agent a)
    {
    	return agents.contains(a);
    }
    
    public void add_agent(Agent r) {
    	if(agents.size() > 1)
    	{
    		r.setCaptain(agents.get(0));
    	}
        agents.add(r);
        r.clearSensors();
        r.group_id = this.group_id;
    }


    public void remove_agent(Agent r) {
        agents.remove(r);
    }

    public ArrayList<Agent> get_agents() {
        return this.agents;
    }

    public int get_id() {
        return this.group_id;
    }

    public Agent get_agent(int agent_id) {
        return this.agents.get(agent_id);
    }

    public int size() {
        return agents.size();
    }

    /**
     * Force all agents to take a step
     */
    public void step(Dimensions d) {
        for (Agent a : agents) {
            a.step(d, agents);
        }
    }

    public void set_human_input(int index)
    {
    	// we have agents, command is up, down, left or right
        if(agents.size() > 0 && index < 14)
        {
        	agents.get(0).set_human_input(index);
        } else // command is split or merge
        {
        	for(Agent a : agents)
        	{
        		a.set_human_input(index);
        	}
        }
        	
    }
}
