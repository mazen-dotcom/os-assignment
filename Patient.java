
public class Patient extends Thread {

    private final int id;
    private final WaitingRoom waitingRoom;

    public Patient(int id, WaitingRoom waitingRoom) {
        this.id = id;
        this.waitingRoom = waitingRoom;
    }

    public int getPatientId() {
        return id;
    }

    @Override
    public void run() {

        System.out.println("P" + id + " arrived.");

        try {
            waitingRoom.enter(this);

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();
        }
    }
}
