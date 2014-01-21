package view;

import view.renderers.Renderer;

import javax.swing.*;
import java.awt.*;
import java.util.List;


public class RendererStack extends JPanel {
    /**
	 * 	SVUID for eclipse.
	 */
	private static final long serialVersionUID = 3076209045873029131L;
	protected final List<Renderer> renderers;

    public RendererStack(List<Renderer> renderers) {
        this.renderers = renderers;
    }

    @Override
    protected void paintComponent(Graphics g) {
        final Graphics2D gr = (Graphics2D) g;
        gr.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        for (Renderer r : renderers)
            r.render(gr);
    }
}
