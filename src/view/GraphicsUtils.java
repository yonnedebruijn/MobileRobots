package view;

import state.Agent;
import state.Parameters;

import java.awt.*;


public class GraphicsUtils {

    public static void drawAgent(Graphics2D gr, Agent ag, Parameters p) {
        final double ax = ag.get_pos().get_x();
        final double ay = ag.get_pos().get_y();

        final double hsize = p.agent_size / 2;
        gr.drawOval((int) (ax - hsize), (int) (ay - hsize), p.agent_size, p.agent_size);

    }
}
