
import java.util.Random;
import java.util.concurrent.CountDownLatch;
public class Doctor extends Thread {
    private int doctorId;
    private WaitingRoom waitingroom;
    private TreatmentRooms treatmentrooms;
    private CountDownLatch allPatientsTreated;

    public Doctor(int id, WaitingRoom w,
                  TreatmentRooms t,
                  CountDownLatch latch) {

        this.doctorId = id;
        this.waitingroom = w;
        this.treatmentrooms = t;
        this.allPatientsTreated = latch;
    }

    @Override
    public void run() {

        try {

            while (!isInterrupted()) {
                Patient p = waitingroom.getPatient();
                boolean acquired = false;
                try {
                    System.out.println(
                        "Doctor " + doctorId +
                        " assigned to P" + p.getPatientId()
                    );
                    acquired =
                        treatmentrooms.acquireRoom(doctorId, 3000);
                    if (!acquired) {
                        System.out.println(
                            "Doctor " + doctorId +
                            " is sending P" + p.getPatientId() +
                            " back to the waiting room."
                        );
                        waitingroom.enter(p);
                        continue;
                    }
                    System.out.println(
                        "Doctor " + doctorId +
                        " begins treating P" + p.getPatientId()
                    );

                    Random random = new Random();
                    int time = random.nextInt(5) + 1;
                    Thread.sleep(time * 1000);
                    System.out.println(
                        "Doctor " + doctorId +
                        " finished treating P" + p.getPatientId()
                    );
                    System.out.println(
                        "P" + p.getPatientId() + " left."
                    );
                    allPatientsTreated.countDown();
                } finally {
                    if (acquired) {
                        treatmentrooms.releaseRoom(doctorId);
                    }
                }
            }

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();

            System.out.println(
                "Doctor " + doctorId + " stopped."
            );
        }
    }
}
