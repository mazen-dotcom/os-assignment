
public class TreatmentRooms {

    private final Semaphore rooms;

    public TreatmentRooms(int numberOfRooms) {
        this.rooms = new Semaphore(numberOfRooms);
    }

    public boolean acquireRoom(int doctorId, long waitingTime) {
        Hospital.log("Doctor " + doctorId + " is trying to acquire a treatment room.");

        boolean acquired;
        try {
            acquired = rooms.tryAcquire(waitingTime);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            acquired = false;
        }

        if (acquired) {
            Hospital.log("Doctor " + doctorId + " acquired a treatment room.");
        } else {
            Hospital.log("Doctor " + doctorId + " could not acquire a treatment room.");
        }
        return acquired;
    }

    public void releaseRoom(int doctorId) {
        rooms.release();
        Hospital.log("Doctor " + doctorId + " released the treatment room.");
    }
}
