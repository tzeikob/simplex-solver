package com.tkb.simplex.model;

import org.apache.log4j.Logger;

/**
 * An interactive graphic object modeling an animated behavior.
 *
 * @author Akis Papadopoulos
 */
public abstract class Animation extends AbstractObject implements Runnable {

    private static final Logger logger = Logger.getLogger(Animation.class);

    protected Thread thread;

    protected volatile boolean suspended;

    protected volatile boolean stopped;

    protected static volatile boolean repeating = false;

    protected static long delay = 1500L;

    protected int frameno;

    protected int frames;

    public Animation() {
        super();

        thread = new Thread(this);

        stopped = false;
        suspended = true;

        frameno = 0;

        thread.start();
    }

    public synchronized void play() {
        if (thread.isAlive()) {
            suspended = false;
            notify();
        } else {
            thread = new Thread(this);

            stopped = false;
            suspended = false;
            notify();

            if (frameno == frames) {
                frameno = 0;
            }

            thread.start();
        }
    }

    public synchronized void pause() {
        suspended = true;
    }

    public synchronized void stop() {
        stopped = true;
        suspended = false;
        notify();

        frameno = 0;
    }

    public synchronized void forward() {
        if (frameno < frames) {
            frameno++;
        }
    }

    public synchronized void backward() {
        if (frameno > 0) {
            frameno--;
        }
    }

    public static void setDelay(long d) {
        delay = Math.abs(d);
    }

    public static void setDelay(double d) {
        long dl = (long) Math.abs(d * 1000);

        delay = dl;
    }

    public static void setRepeating(boolean flag) {
        repeating = flag;
    }

    public static boolean isRepeating() {
        return repeating;
    }

    public void dispose() {
        stopped = true;
        thread = null;
    }

    public void join() {
        try {
            thread.join();
        } catch (InterruptedException exc) {
            logger.error(exc.getMessage(), exc);
        }
    }

    public boolean isAlive() {
        return thread.isAlive();
    }
}
