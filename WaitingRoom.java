import java.util.LinkedList;
import java.util.Queue;

public class WaitingRoom {

    private Queue<Patient> patients;

    private Semaphore emptySeats;
    private Semaphore availablePatients;

    private final Object queueLock = new Object();

    public WaitingRoom(int capacity) {

        patients = new LinkedList<>();

        emptySeats = new Semaphore(capacity);
        availablePatients = new Semaphore(0);
    }

    // Patient enters the waiting room
    public void enter(Patient p) throws InterruptedException {

        System.out.println(
            "P" + p.getPatientId()
            + " is trying to enter the waiting room."
        );

        // Wait if there is no available seat
        emptySeats.acquire();

        synchronized (queueLock) {

            patients.add(p);

            System.out.println(
                "P" + p.getPatientId()
                + " entered the waiting room."
            );
        }

        // Notify doctors that a patient is available
        availablePatients.release();
    }

    // Doctor takes a patient from the waiting room
    public Patient getPatient() throws InterruptedException {

        // Wait if there are no patients
        availablePatients.acquire();

        Patient p;

        synchronized (queueLock) {

            p = patients.poll();

            System.out.println(
                "Doctor took P" + p.getPatientId()
                + " from the waiting room."
            );
        }

        // Release the seat after taking the patient
        emptySeats.release();

        return p;
    }

    public int getSize() {

        synchronized (queueLock) {
            return patients.size();
        }
    }
}
