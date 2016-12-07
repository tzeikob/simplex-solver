package com.tkb.simplex.model;

import java.awt.Color;
import org.apache.log4j.Logger;

/**
 * A graphic object representing a 2D feasible region as an animated visual
 * object.
 *
 * @author Akis Papadopoulos
 */
public class Feasible2DRegion extends Animation {

    private static final Logger logger = Logger.getLogger(Feasible2DRegion.class);

    private double adarap;

    private Edge[] inequalities;

    private MarkDot[] infeasibles;

    private MarkDot[] feasibles;

    private Edge[] rays;

    private Polygon region;

    private Edge[] objectives;

    public Feasible2DRegion(double adarap, Edge[] inequalities,
            MarkDot[] infeasibles, MarkDot[] feasibles, Edge[] rays,
            Polygon region, Edge[] objectives) {
        super();

        this.adarap = adarap;
        this.inequalities = inequalities;
        this.infeasibles = infeasibles;
        this.feasibles = feasibles;
        this.rays = rays;
        this.region = region;
        this.objectives = objectives;

        frames = inequalities.length + 2;
        if (adarap != -1) {
            frames += feasibles.length + 1 + objectives.length;

            if (adarap == 0) {
                frames++;
            }
        }
    }

    @Override
    public void render(AbstractView view) {
        ObjectGroup scene = new ObjectGroup();

        if (adarap != -1 && frameno >= inequalities.length + 2 + feasibles.length + 1 && region != null) {
            region.setBackground(Color.yellow);
            scene.addObject(region);
        }

        if (frameno >= inequalities.length + 1 && rays != null) {
            for (int i = 0; i < rays.length; i++) {
                rays[i].setForeground(Color.pink);
                scene.addObject(rays[i]);
            }
        }

        if (frameno >= 1 && inequalities != null) {
            for (int i = 1; i <= inequalities.length; i++) {
                if (frameno >= i) {
                    inequalities[i - 1].setBackground(Color.orange);
                    scene.addObject(inequalities[i - 1]);
                }
            }
        }

        if (frameno >= inequalities.length + 2 && infeasibles != null) {
            for (int i = 0; i < infeasibles.length; i++) {
                infeasibles[i].setBackground(Color.lightGray);
                scene.addObject(infeasibles[i]);
            }
        }

        if (adarap != -1 && frameno >= inequalities.length + 2 + feasibles.length + 2 && objectives != null) {
            int covered = inequalities.length + 2 + feasibles.length + 1;
            for (int i = covered + 1; i <= covered + objectives.length; i++) {
                if (frameno >= i) {
                    objectives[i - (covered + 1)].setBackground(Color.cyan);
                    objectives[i - (covered + 1)].setOpacity((255 / objectives.length) * (i - (covered + 1) + 1) + 10);
                    scene.addObject(objectives[i - (covered + 1)]);
                }
            }
        }

        if (adarap != -1 && frameno >= inequalities.length + 2 + 1 && feasibles != null) {
            int covered = inequalities.length + 2;
            for (int i = covered + 1; i <= covered + feasibles.length; i++) {
                if (frameno >= i) {
                    feasibles[i - (covered + 1)].setBackground(Color.cyan);
                    scene.addObject(feasibles[i - (covered + 1)]);
                }
            }
        }

        if (adarap == 0 && frameno >= frames && feasibles != null && objectives != null) {
            objectives[objectives.length - 1].setBackground(Color.green);

            feasibles[feasibles.length - 1].setBackground(Color.green);
        }

        scene.render(view);

        view.initialize();
    }

    @Override
    public void transform(SpaceMatrix m) {
        if (inequalities != null) {
            for (int i = 0; i < inequalities.length; i++) {
                inequalities[i].transform(m);
            }
        }

        if (infeasibles != null) {
            for (int i = 0; i < infeasibles.length; i++) {
                infeasibles[i].transform(m);
            }
        }

        if (feasibles != null) {
            for (int i = 0; i < feasibles.length; i++) {
                feasibles[i].transform(m);
            }
        }

        if (rays != null) {
            for (int i = 0; i < rays.length; i++) {
                rays[i].transform(m);
            }
        }

        if (region != null) {
            region.transform(m);
        }

        if (objectives != null) {
            for (int i = 0; i < objectives.length; i++) {
                objectives[i].transform(m);
            }
        }
    }

    @Override
    public void run() {
        while (frameno <= frames) {
            try {
                synchronized (this) {
                    while (suspended) {
                        wait();
                    }

                    if (stopped) {
                        frameno = 0;
                        return;
                    }
                }

                thread.sleep(1500);

                if (frameno < frames) {
                    frameno++;
                } else {
                    if (repeating) {
                        frameno = 0;
                    } else {
                        return;
                    }
                }
            } catch (InterruptedException exc) {
                logger.error(exc.getMessage(), exc);
            }
        }
    }
}
