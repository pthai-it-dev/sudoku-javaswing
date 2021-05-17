package thread;

import controller.ThreadController;

import jaco.mp3.player.MP3Player;

import java.io.File;
import java.util.concurrent.TimeUnit;

public class MusicThread extends Thread
{
    private final ThreadController threadController;

    private volatile boolean forceStop = true;
    private volatile boolean forceSkip = false;


    public MusicThread(ThreadController threadController)
    {
        this.threadController = threadController;
    }

    public boolean isForceStop()
    {
        return forceStop;
    }

    public void setForceStop(boolean forceStop)
    {
        this.forceStop = forceStop;
    }

    public void setForceSkip(boolean forceSkip)
    {
        this.forceSkip = forceSkip;
    }

    @Override
    public synchronized void run()
    {
        MP3Player mp3 = new MP3Player();
        for (int i = 1; i < 13; i++)
        {
            mp3.addToPlayList(new File("D:/Sudoku/src/music/" + i + ".mp3"));
        }
        mp3.setShuffle(true);
        mp3.play();

        while (true)
        {
            try
            {
                if (forceStop)
                {
                    mp3.pause();
                    threadController.hold();
                }

                if ((mp3.isStopped() || mp3.isPaused()) &&
                    !forceStop)
                {
                    mp3.play();
                }

                if (forceSkip)
                {
                    mp3.skipForward();
                    forceSkip = false;
                }

                TimeUnit.MILLISECONDS.sleep(200);

            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }

    public void wake()
    {
        threadController.wake();
    }
}
