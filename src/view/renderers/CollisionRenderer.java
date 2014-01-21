package view.renderers;

import state.Agent;
import state.Group;
import state.World;
import view.ColorPair;
import view.GraphicsUtils;

import java.awt.*;


public class CollisionRenderer implements Renderer {
    World world;

    public CollisionRenderer(World world) {
        this.world = world;
    }

    /**
     * Renders the robots from world.jungle to the screen
     * Sets group colors, taken from the ColorTheme.color_list
     */
    public void render(Graphics2D gr) {
        for (Group g : world.get_jungle().get_pool()) {
            ColorPair cp = world.get_color_theme().get_pair(g.get_id());
            gr.setBackground(cp.get_back());
            gr.setColor(cp.get_front());
            for (Agent a : g.get_agents()) {
                GraphicsUtils.drawAgent(gr, a, a.get_params());
            }
        }
    }
}
