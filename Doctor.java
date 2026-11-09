import java.util.Random;
import java.util.concurrent.CountDownLatch;

public class Doctor extends Thread {

    private final int doctorId;
    private final WaitingRoom waitingRoom;
    private final TreatmentRooms treatmentRooms;
    private final CountDownLatch allPatientsTreated;
    private final Random random = new Random();

    public Doctor(int id, WaitingRoom w, TreatmentRooms t, CountDownLatch latch) {
        this.doctorId = id;
        this.waitingRoom = w;
        this.treatmentRooms = t;
        this.allPatientsTreated = latch;
    }

    @Override
    public void run() {
        try {
            while (!isInterrupted()) {

                Patient p = waitingRoom.getPatient();
                System.out.println("Doctor " + doctorId
                        + " is assigned to P" + p.getPatientId() + ".");

                boolean acquired = treatmentRooms.acquireRoom(doctorId, 3000);

                if (!acquired) {
                    System.out.println("Doctor " + doctorId + " is sending P"
                            + p.getPatientId() + " back to the waiting room.");
                    sendBack(p);
                    System.out.println("Doctor " + doctorId + " is now available.");
                    continue;
                }

                try {
                    System.out.println("Doctor " + doctorId
                            + " begins treating P" + p.getPatientId() + ".");
                    Thread.sleep((random.nextInt(5) + 1) * 1000L);
                    System.out.println("Doctor " + doctorId
                            + " finished treating P" + p.getPatientId() + ".");
                } finally {
                    treatmentRooms.releaseRoom(doctorId);
                }

                System.out.println("Doctor " + doctorId + " is now available.");
                System.out.println("P" + p.getPatientId() + " left.");
                allPatientsTreated.countDown();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); 
        }
    }

    private void sendBack(Patient p) {
        Thread t = new Thread(() -> {
            try {
                waitingRoom.enter(p);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        t.start();
    }
}
