package state;

import java.util.Random;

/**
 * Combination of two Doubles
 * Created by Yonne on 05/12/13.
 */
public class Coordinate {
    private double x;
    private double y;

    /**
     * @param x
     * @param y
     */
    public Coordinate(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Coordinate with random values within given dimensions
     *
     * @param random     Random number generator to take values from
     * @param dimensions
     */
    public Coordinate(Random random, Dimensions dimensions) {
        setRandomCoordinate(random, dimensions);
    }

    /**
     * @return this.x
     */
    public double get_x() {
        return this.x;
    }

    /**
     * @return this.y
     */
    public double get_y() {
        return this.y;
    }

    /**
     * Adds a Coordinate to this Coordinate
     *
     * @param c Coord to be added
     * @return new Coordinate
     */
    public Coordinate add(Coordinate c) {
        this.x += c.get_x();
        this.y += c.get_y();
        return this;

    }

    /**
     * Scales a Coordinate by a given scalar
     *
     * @param scalar Multiplier for the scale function
     * @return
     */
    public Coordinate scale(double scalar) {
        x *= scalar;
        y *= scalar;
        return this;
    }

    /**
     * Calculates the relative distance from this Coordinate
     * to a given one
     *
     * @param c Coordinate to compare to
     * @return double relative distance
     */
    public double distance(Coordinate c) {
        final double dx = StrictMath.abs(x - c.x);
        final double dy = StrictMath.abs(y - c.y);
        return StrictMath.sqrt(dx * dx + dy * dy);
    }

    /**
     * Bounds a Coordinate within a given dimension
     *
     * @param dim Dimension-object to be bound within
     * @return return this Coordinate
     */
    public void bound(int agent_size, Dimensions dim) {
        /* 
         * 24 is p.agent_size, the circles of the agents are drawn from
    	 * the bottom-right (e.g. the coordinate position), they are 24 pixels wide
    	 * so we have to find a value between 24 and dim.width/height.  
    	 */
        x = StrictMath.max(agent_size, StrictMath.min(dim.width, x));
        y = StrictMath.max(agent_size, StrictMath.min(dim.height, y));
    }

    /**
     * Fills a Coordinate-object with random x,y
     *
     * @param random     Random number generator to take values from
     * @param dimensions Dimension-object to bound within
     */
    public void setRandomCoordinate(Random random, Dimensions dimensions) {
        x = random.nextDouble() * dimensions.width;
        y = random.nextDouble() * dimensions.height;
    }

    /**
     * Rounds a coordinate for nicer viewing (Logging Purposes)
     *
     * @param scalar a 1 followed by the number of decimals
     * @return
     */
    public Coordinate round(int scalar) {
        return new Coordinate(((double) Math.round(this.x * scalar) / scalar), ((double) Math.round(this.y * scalar) / scalar));
    }
}
