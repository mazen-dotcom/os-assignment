
public class Semaphore {

    private int value;

    public Semaphore(int v) {
        value = v;
    }

    public synchronized void acquire()
            throws InterruptedException {

        while (value == 0) {
            wait();
        }

        value--;
    }

    public synchronized void release() {
        value++;
        notifyAll();
    }

    public synchronized boolean tryAcquire(long timeoutMillis)
            throws InterruptedException {

        if (timeoutMillis < 0) {
            throw new IllegalArgumentException("Invalid timeout");
        }

        long deadline = System.nanoTime()
                + timeoutMillis * 1_000_000L;

        while (value == 0) {

            long remaining = deadline - System.nanoTime();

            if (remaining <= 0) {
                return false;
            }

            long millis = remaining / 1_000_000L;
            int nanos = (int) (remaining % 1_000_000L);

            wait(millis, nanos);
        }

        value--;
        return true;
    }
}
