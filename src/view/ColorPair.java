package view;

import java.awt.*;

/**
 * Created by Yonne on 02/01/14.
 * Pairs up two java.awt.Color objects
 */

public class ColorPair {
    Color front;
    Color back;

    public ColorPair(Color bg, Color fg) {
        this.front = fg;
        this.back = bg;
    }

    public Color get_front() {
        return this.front;
    }

    public Color get_back() {
        return this.back;
    }
}
