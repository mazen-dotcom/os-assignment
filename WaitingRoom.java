
import java.util.LinkedList;
import java.util.Queue;

public class WaitingRoom {

    private final Queue<Patient> patients;

    private final Semaphore emptySeats;
    private final Semaphore availablePatients;

    private final Object queueLock = new Object();

    public WaitingRoom(int capacity) {

        patients = new LinkedList<>();

        emptySeats = new Semaphore(capacity);
        availablePatients = new Semaphore(0);
    }

    public void enter(Patient p) throws InterruptedException {

        boolean seatAcquired = emptySeats.tryAcquire(0);

        if (!seatAcquired) {

            System.out.println("The waiting room is full.");

            System.out.println(
                "P" + p.getPatientId()
                + " is waiting outside for an available seat."
            );

            emptySeats.acquire();
        }

        synchronized (queueLock) {

            patients.add(p);

            System.out.println(
                "P" + p.getPatientId()
                + " entered the waiting room."
            );
        }

        availablePatients.release();
    }

    public Patient getPatient(int doctorId)
            throws InterruptedException {

        availablePatients.acquire();

        Patient p;

        synchronized (queueLock) {
            p = patients.poll();
        }

        emptySeats.release();

        return p;
    }

 
}
