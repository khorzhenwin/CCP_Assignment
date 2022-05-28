
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
         System.out.println(Main.ANSI_BLUE_BACKGROUND + "ATC: Cleaning crew will now begin refilling supplies for " + plane.getPlaneName() + Main.ANSI_RESET);
         TimeUnit.SECONDS.sleep(2);
         System.out.println(Main.ANSI_BLUE_BACKGROUND + "------------------------------ Refilling supplies for " + plane.getPlaneName() + " ------------------------------" + Main.ANSI_RESET);
         TimeUnit.SECONDS.sleep(2);
         System.out.println(Main.ANSI_BLUE_BACKGROUND + "------------------------------ Cleaning aircraft for " + plane.getPlaneName() + " ------------------------------" + Main.ANSI_RESET);
         TimeUnit.SECONDS.sleep(2);
         System.out.println(Main.ANSI_BLUE_BACKGROUND + "ATC: Cleaning crew is done cleaning " + plane.getPlaneName() + Main.ANSI_RESET);
         System.out.println(Main.ANSI_BLUE_BACKGROUND + "Cleaning crew is leaving " + plane.getPlaneName() + "..." + Main.ANSI_RESET);
      } catch (InterruptedException ex) {
         Logger.getLogger(Cleaner.class.getName()).log(Level.SEVERE, null, ex);
      }

   }
}
