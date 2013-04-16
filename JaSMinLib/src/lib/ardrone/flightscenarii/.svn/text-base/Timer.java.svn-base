package lib.ardrone.flightscenarii;

public class Timer extends Thread {

    private Thread count;
    private TagFollowing suiviTag;

    public Timer(TagFollowing st) {
        suiviTag = st;
        count = new Thread(this);
        count.start();
    }

    public void run() {

        try {
            count.sleep(10000);
            //my_thread_user.over();
        } catch (Exception e) {
        }
        //suiviTag.kill = true;
    }

    public void over() {
        count.stop();
    }
}
