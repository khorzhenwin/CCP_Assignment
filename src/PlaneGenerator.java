
import java.util.concurrent.TimeUnit;

public class PlaneGenerator implements Runnable {

   Airport airport;
   public boolean closed = false;

   public PlaneGenerator(Airport airport) {
      this.airport = airport;
   }

   public void run() {
      while (!closed) {
         Plane plane = new Plane(airport);
         Thread planeThread = new Thread(plane);
         plane.setPlaneName("Plane " + planeThread.getId());
         planeThread.start();

         try {
            TimeUnit.SECONDS.sleep((long) (Math.random() * 10));
         } catch (InterruptedException iex) {
            iex.printStackTrace();
         }
      }
      if (closed) {
         try {
            Thread.sleep(5000);
            System.out.println("Clearing queue ...");
            Plane plane = new Plane(airport);
            Thread planeThread = new Thread(plane);
            plane.setPlaneName("Plane " + planeThread.getId());
            planeThread.start();
         } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
         }
         return;
      }
   }

   public synchronized void setClosing() {
      closed = true;
      System.out.println("Printing report for the past 6 planes ...");
   }
}
