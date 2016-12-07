package com.tkb.simplex.model;

/**
 * An abstract convex hull graphic object.
 *
 * @author Akis Papadopoulos
 */
public abstract class ConvexHull extends AbstractObject {

    protected SpacePoint[] data;

    protected SpacePoint[] vertices;

    public ConvexHull() {
        super();
    }

    /**
     * A method to build a convex hull object.
     */
    public abstract void build();
}
