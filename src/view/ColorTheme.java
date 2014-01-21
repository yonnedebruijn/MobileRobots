package view;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Yonne on 02/01/14.
 * Defines the colors in ColorPairs for the groups
 * Saves them in an ArrayList for easy reference by group_id
 */

public class ColorTheme {

    ArrayList<ColorPair> color_list = new ArrayList<ColorPair>();
    ColorPair world_color = new ColorPair(new Color(2, 95, 200, 128), new Color(0, 9, 200, 128));

    // Group Colors
    ColorPair group_1 = new ColorPair(new Color(247, 255, 0, 255), new Color(222, 240, 15, 255));

    public ColorTheme() {
        // color_list.add(group_1);
    }

    public ArrayList<ColorPair> get_colors() {
        return this.color_list;
    }

    public ColorPair get_pair(int group_id) {
        return this.color_list.get(group_id-1);
    }

    public ColorPair get_world() {
        return world_color;
    }

    public Color random_color() {
        Random rand = new Random();
        // keep randomly generating colours until it is not black-ish.
        while(true)
        {
	        float r = rand.nextFloat();
	        float g = rand.nextFloat();
	        float b = rand.nextFloat();
	        
	        // 0.30 is an educated guess, it works and produces god-ugly results. 
	        if(r+g+b > 0.30)
	        {
	        	return new Color(r,g,b);
	        }
        }
    }

    /**
     * Creates a new random color, adds it to the color_list, and returns a new
     * group_id for the new Group-object to use.
     *
     * @return the size of the color_list = the group_id of the new group
     */
    public int new_group_color() {
        ColorPair cp = new ColorPair(random_color(), random_color());
        color_list.add(cp);
        return color_list.size();
    }
}







