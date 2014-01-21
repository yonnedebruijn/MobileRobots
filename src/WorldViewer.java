import state.Dimensions;
import state.World;
import view.RendererStack;
import view.renderers.CollisionRenderer;
import view.renderers.Renderer;
import view.renderers.WorldRenderer;

import java.awt.*;
import java.util.ArrayList;

// supress this warning.
@SuppressWarnings("serial")
public class WorldViewer extends RendererStack {

    public WorldViewer(World world) {
        super(new ArrayList<Renderer>());

        final Dimensions d = new Dimensions(world.get_dimension()).add(48, 48);
        setPreferredSize(new Dimension(d.get_width(), d.get_height()));

        renderers.add(new WorldRenderer(world));
        renderers.add(new CollisionRenderer(world));

    }
}