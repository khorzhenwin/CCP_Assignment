
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Cleaner implements Runnable {

   Plane plane;
   int id;
   String cleanerName;

   public Cleaner(Plane plane) {
      this.plane = plane;
   }

   public Plane getPlane() {
      return plane;
   }

   public void setPlane(Plane plane) {
      this.plane = plane;
   }

   public int getId() {
      return id;
   }

   public void setId(int id) {
      this.id = id;
   }

   public String getCleanerName() {
      return cleanerName;
   }

   public void setCleanerName(String cleanerName) {
      this.cleanerName = cleanerName;
   }

   public void run() {
      try {
         TimeUnit.SECONDS.sleep(1);
         System.out.println("ATC: Cleaning crew will now begin refilling supplies for " + plane.getPlaneName());
         TimeUnit.SECONDS.sleep(2);
         System.out.println("------------------------------Refilling supplies for " + plane.getPlaneName() + "------------------------------");
         TimeUnit.SECONDS.sleep(2);
         System.out.println("------------------------------Cleaning aircraft for " + plane.getPlaneName() + "------------------------------");
         TimeUnit.SECONDS.sleep(2);
         System.out.println("ATC: Cleaning crew is done cleaning " + plane.getPlaneName());
         System.out.println("Cleaning crew is leaving " + plane.getPlaneName() + "...");
      } catch (InterruptedException ex) {
         Logger.getLogger(Cleaner.class.getName()).log(Level.SEVERE, null, ex);
      }

   }
}
