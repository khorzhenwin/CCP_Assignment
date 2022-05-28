
import java.util.concurrent.TimeUnit;

public class PlaneGenerator implements Runnable {

   Airport airport;

   public PlaneGenerator(Airport airport) {
      this.airport = airport;
   }

   public void run() {

      for (int i = 0; i < 6; i++) {

         Plane plane = new Plane(airport);
         Thread planeThread = new Thread(plane);
         plane.setPlaneName("Plane " + planeThread.getId());
         planeThread.start();

         try {
            TimeUnit.SECONDS.sleep(5);
         } catch (InterruptedException iex) {
            iex.printStackTrace();
         }
      }

      return;
   }
}
