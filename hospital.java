import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
public class Hospital {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.print("Enter waiting room capacity: ");
        int capacity = input.nextInt();
        System.out.print("Enter number of doctors: ");
        int doctorCount = input.nextInt();
        System.out.print("Enter number of patients: ");
        int patientCount = input.nextInt();
        if (capacity <= 0 || doctorCount <= 0
                || patientCount < 0) {
            System.out.println("Invalid input.");
            input.close();
            return;
        }
        System.out.println("\nHospital is starting...\n");
        WaitingRoom waitingRoom = new WaitingRoom(capacity);
        TreatmentRooms treatmentRooms = new TreatmentRooms(2);
        CountDownLatch allPatientsTreated =
                new CountDownLatch(patientCount);
        Doctor[] doctors = new Doctor[doctorCount];
        for (int i = 0; i < doctorCount; i++) {
            doctors[i] = new Doctor(
                i + 1,
                waitingRoom,
                treatmentRooms,
                allPatientsTreated
            );
        }
        Patient[] patients = new Patient[patientCount];
        for (int i = 0; i < patientCount; i++) {
            patients[i] = new Patient(
                i + 1,
                waitingRoom
            );
        }
        for (int i = 0; i < doctorCount; i++) {
            doctors[i].start();
        }
        for (int i = 0; i < patientCount; i++) {
            patients[i].start();
        }

        try {
            for (int i = 0; i < patientCount; i++) {
                patients[i].join();
            }
            allPatientsTreated.await();
            System.out.println("\nAll patients have been treated.");
        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();

        } finally {


            for (int i = 0; i < doctorCount; i++) {
                doctors[i].interrupt();
            }

            for (int i = 0; i < doctorCount; i++) {

                try {
                    doctors[i].join();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }

        System.out.println("\nHospital simulation finished.");

        input.close();
    }
}
