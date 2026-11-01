public class Patient extends Thread {

    private int id;
    private WaitingRoom waitingRoom;

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

            System.out.println(
                "P" + id + " was interrupted while waiting."
            );
        }
    }
}
