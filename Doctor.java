import java.util.Random;
public class Doctor extends Thread{
    private int    doctorId;
    private WaitingRoom  waitingroom;
    private   TreatmentRooms   treatmentrooms;
   private int RandomGenrator;

   public Doctor(int id, WaitingRoom w, TreatmentRooms t){
       this.doctorId = id;
       this.waitingroom = w;
       this.treatmentrooms = t;
   }
   @Override
   public void run(){
       boolean acquired=false;
       try {
           Patient p= waitingroom.getPatient() ;
           System.out.println("Doctor" + doctorId + " assigned to "+"P"+p.getPatientId());
          acquired=treatmentrooms.acquireRoom(doctorId,3);
            if(acquired){
                Random random = new Random();
                System.out.println("Doctor" + doctorId + " begins treating "+"P"+p.getPatientId());
                int time = random.nextInt(5) + 1;
                Thread.sleep(time * 1000);
                System.out.println("Doctor" + doctorId + " finished treating "+"P"+p.getPatientId());
                System.out.println("Doctor " + doctorId + " released the treatment room.");
                System.out.println("Doctor" + doctorId + " is now available.");
                System.out.println("P" + p.getPatientId() + "  left");
            }else{
            waitingroom.enter(p);
            System.out.println("Doctor" + doctorId + " is sending " +"P"+p.getPatientId()+" back to the waiting room.");
            }
       } catch (InterruptedException e) {
           Thread.currentThread().interrupt();
       }finally {
           if (acquired==true) {
               treatmentrooms.releaseRoom(doctorId);
           }
       }
   }


}
