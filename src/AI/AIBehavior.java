package AI;

public abstract class AIBehavior implements Runnable {
    boolean isStop = false;

    public abstract void onFrame(long dt);

    public void setStop() {
        isStop = true;
    }

    @Override
    public void run() {

    }
}
