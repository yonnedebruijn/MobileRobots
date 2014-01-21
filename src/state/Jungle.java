package state;

import view.ColorTheme;
import view.LoggingPanel;

import java.util.ArrayList;

/**
 * Class that holds the ArrayList<Group> containing the
 * Group-objects with Agent-objects
 * Created by Yonne on 05/12/13.
 */

public class Jungle {
    ArrayList<Group> pool = new ArrayList<Group>();
    ArrayList<Agent> toRemove = new ArrayList<Agent>();
    ArrayList<Agent> toMove = new ArrayList<Agent>();
    Group focus_group;
    LoggingPanel debug;
    LoggingPanel log;
    ColorTheme ct;

    public Jungle(ColorTheme ct, LoggingPanel debug,LoggingPanel log) {
        super();
        this.debug = debug;
        this.log = log;
        this.ct = ct;
    }

    public void add_group(Group group) {
        pool.add(group);
        ct.new_group_color();

    }
    
    public void clearPool()
    {
    	// clear the pool.
    	pool = new ArrayList<Group>();
    }

    public void remove_group(Group group) {
        pool.remove(group);
    }

    public void remove_agent(int group_id, Agent agent) {
        toRemove.add(agent);
    }
    
    public void addToOtherGroup(Agent a)
    {
    	toMove.add(a);
    }

    /**
     * Returns a group specified by its group_id
     *
     * @param group_id Group we want to get
     * @return Group if found
     * @throws Exception
     */
    public Group get(int group_id) throws Exception {
        for (int i = 0; i < pool.size(); i++) {
            if (pool.get(i).group_id == group_id) {
                return pool.get(i);
            }
        }
        throw new JungleException(debug, pool.size(), group_id);
    }

    public ArrayList<Group> get_pool() {
        return this.pool;
    }


    public int pool_size() {
        return pool.size();
    }

    /**
     * Force all groups to take a step
     */
    public void step(Dimensions boundaries) {
        for (int i = 0; i < pool.size(); i++) {
            pool.get(i).step(boundaries);
        }
        // update all agent stuff. 
        if(toRemove.size() != 0)
	    {
        	for(Agent a : toRemove)
	        {
	        	pool.get(a.group_id-1).remove_agent(a);
	        }
	        Group newGroup = new Group(pool.size()+1, toRemove, debug, log, this);
	        add_group(newGroup);
	        toRemove = new ArrayList<Agent>();
    	}
        // if we have agents to move, and there are more groups, do it.
        if(toMove.size() != 0 && pool.size() > 1)
        {
        	int groupToRemove = toMove.get(0).group_id;
        	int i_num = 0;
        	// find which index the group is in.
        	for(int i = 0; i < pool.size(); i++)
        	{
        		if(pool.get(i).group_id == groupToRemove)
        		{
        			i_num = i;
        			break;
        		}
        	}
        	// 
        	if(i_num+1 < pool.size())
        	{
        		for(int i = 0; i < toMove.size(); i++)
        		{
        			pool.get(i_num+1).add_agent(toMove.get(i));
        		}
        	} else {
        		for(int i = 0; i < toMove.size(); i++)
        		{
        			pool.get(i_num-1).add_agent(toMove.get(i));
        		}
        	}
        	remove_group(pool.get(i_num));
        	
        	toMove = new ArrayList<Agent>();
        }
    }

    /**
     * Returns a specific agent from a given group
     *
     * @param group_id The group number
     * @param agent_id Which agent we want to get
     * @return Agent-object from given group
     */
    public Agent get_agent(int group_id, int agent_id) throws Exception {
        return pool.get(group_id).get_agent(agent_id);
    }

    /**
     * Sets what group is in focus (who is in action)
     * @param group_id
     * @return  The current focused group
     */
    public Group set_focus(int group_id)
    {
        focus_group = pool.get(group_id-1);
        log.write("Currently focused group: Group " + (group_id+1),ct.get_pair(group_id).get_front());
        return focus_group;
    }

    public void set_human_input(int index, int group)
    {
        try
        {
        	pool.get(group-1).set_human_input(index);
        } catch (IndexOutOfBoundsException e)
        {
        	// group number doesn't exist, do nothing.
        }
    }

}
