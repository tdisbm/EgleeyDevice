package tasks;

public abstract class Task implements Runnable
{
    private int rate;

    final public Task setRate(int rate) {
        this.rate = rate;

        return this;
    }

    final public int getRate() {
        return this.rate;
    }
}
