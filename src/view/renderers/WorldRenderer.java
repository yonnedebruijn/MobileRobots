package view.renderers;

import state.World;

import java.awt.*;

public class WorldRenderer implements Renderer {
    final World world;


    public WorldRenderer(World world) {
        this.world = world;
    }

    public void render(Graphics2D gr) {
        final double w = world.get_width();
        final double h = world.get_height();

        final int S = 12;

        gr.setBackground(world.get_color_theme().get_world().get_front());
        gr.fillRect(S, S, (int) w + S, (int) h + S);

        gr.setColor(world.get_color_theme().get_world().get_back());
        gr.drawRect(S, S, (int) w + S, (int) h + S);
    }
}
