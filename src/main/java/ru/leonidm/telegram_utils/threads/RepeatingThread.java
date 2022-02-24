package ru.leonidm.telegram_utils.threads;

public abstract class RepeatingThread extends Thread {

    private final long delay;
    private final boolean runImmediately;
    private boolean enabled;

    /**
     * @param delay Delay in milliseconds
     */
    protected RepeatingThread(long delay) {
        this.delay = delay;
        this.runImmediately = false;
        this.enabled = true;
    }

    protected RepeatingThread(long delay, boolean runImmediately) {
        this.delay = delay;
        this.runImmediately = runImmediately;
        this.enabled = true;
    }

    /**
     * Disables thread and stops it
     */
    public void disable() {
        this.enabled = false;
    }

    @Override
    public final void run() {
        long currentTime = System.currentTimeMillis();
        long previousSaveTime = currentTime;

        if(runImmediately) previousSaveTime -= delay;

        while(enabled) {
            while(((currentTime = System.currentTimeMillis()) < previousSaveTime + delay) && enabled);

            previousSaveTime = currentTime;

            if(enabled) {
                runAfterDelay();
            }
        }
    }

    /**
     * This method calls when delay has passed
     */
    public abstract void runAfterDelay();
}
