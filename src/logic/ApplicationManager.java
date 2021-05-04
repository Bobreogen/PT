package logic;

import AI.AIManager;
import gui.WindowMain;

public class ApplicationManager {

    private static long systemTime = 0;
    private final static long MinFrameTime = 1000/60;
    private static long dt = 0;
    static final Thread AIThread;
    static {
        AIThread = new Thread() {
            @Override
            public void run() {
                try {
                    while (true) {
                        AIManager.instance().onFrame(dt);
                        synchronized (this) {
                            wait();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    public static long getFrameTime() { return dt; }


    public static void start() {
        try {
            while (true) {
                long curTime = System.currentTimeMillis();
                dt = curTime - systemTime;
                systemTime = curTime;

                if(AIThread.getState() == Thread.State.NEW)
                    AIThread.start();
                else
                    synchronized (AIThread) {
                        AIThread.notifyAll();
                    }

                Habitat.instance().onFrame(dt);
                WindowMain.Instance().onFrame(dt);

                while(AIThread.getState() != Thread.State.WAITING);

                curTime = System.currentTimeMillis();

                if (curTime - systemTime < MinFrameTime)
                    Thread.sleep(MinFrameTime - curTime + systemTime);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
