public class Semaphore {
    private int value;
    public Semaphore(int v) { value = v; }

    public synchronized void acquire() throws InterruptedException {
        while (value == 0) wait();
        value--;
    }

    public synchronized void release() {
        value++;
        notify();
    }  
}
