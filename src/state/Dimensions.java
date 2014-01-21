package state;

/**
 * Specifies the dimension for
 * the simulation to be used
 * Created by Yonne on 05/12/13.
 */
public class Dimensions {

    final int width;
    final int height;

    /**
     * @param width
     * @param height
     */
    public Dimensions(int width, int height) {
        this.width = width;
        this.height = height;
    }

    // copy constructor
    public Dimensions(Dimensions dimension) {
        this(dimension.width, dimension.height);
    }

    public int get_width() {
        return this.width;
    }

    public int get_height() {
        return this.height;
    }

    public Dimensions add(int width, int height) {
        return new Dimensions(this.width + width, this.height + height);
    }
}
