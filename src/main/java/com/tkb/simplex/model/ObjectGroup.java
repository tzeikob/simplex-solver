package com.tkb.simplex.model;

/**
 * A group of graphic objects.
 * 
 * @author Akis Papadopoulos
 */
public class ObjectGroup extends AbstractObject {

    protected AbstractObject[] data;

    protected int counter;

    protected boolean ordering;

    public ObjectGroup() {
        super();

        data = new AbstractObject[10];

        counter = 0;

        ordering = false;
    }

    public ObjectGroup(boolean ordering) {
        super();

        data = new AbstractObject[10];

        counter = 0;

        this.ordering = ordering;
    }

    public ObjectGroup(int capacity, boolean ordering) {
        super();

        data = new AbstractObject[capacity];

        counter = 0;

        this.ordering = ordering;
    }

    public void addObject(AbstractObject object) {
        if (data.length == counter) {
            AbstractObject[] datanew = new AbstractObject[2 * data.length];

            System.arraycopy(data, 0, datanew, 0, counter);
            data = datanew;
        }

        data[counter++] = object;

        centre = data[0].centre;

        for (int i = 1; i < counter; i++) {
            centre = centre.add(data[i].centre);
        }

        centre = centre.scale(1.0 / counter);
    }

    public AbstractObject objectAt(int i) {
        if (i >= 0 && i < counter) {
            return data[i];
        } else {
            return null;
        }
    }

    public void append(ObjectGroup group) {
        for (int i = 0; i < group.size(); i++) {
            addObject(group.objectAt(i));
        }
    }

    public int size() {
        return counter;
    }

    public int capacity() {
        return data.length;
    }

    @Override
    public void render(AbstractView view) {
        if (ordering) {
            for (int i = 1; i < counter; i++) {
                for (int j = counter - 1; j >= i; j--) {
                    AbstractObject oa = data[j - 1];
                    AbstractObject ob = data[j];

                    double deptha = view.depth(oa.centre);
                    double depthb = view.depth(ob.centre);

                    if (deptha < depthb) {
                        AbstractObject temp = data[j - 1];
                        data[j - 1] = data[j];
                        data[j] = temp;
                    }
                }
            }
        }

        for (int i = 0; i < counter; i++) {
            data[i].render(view);
        }
    }

    @Override
    public void transform(SpaceMatrix m) {
        for (int i = 0; i < counter; i++) {
            data[i].transform(m);
        }

        centre = centre.transform(m);
    }
}
