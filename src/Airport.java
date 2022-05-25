
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Airport {

   int maxQueueSize = 6;
   List<Plane> planeQueue;

   public Airport() {
      planeQueue = new LinkedList<Plane>();
   }

   void joinQueue(Plane plane) {
      System.out.println("ATC: " + plane.getPlaneName() + " is requesting to enter the queue to land.");
      synchronized (planeQueue) {
         if (planeQueue.size() == maxQueueSize) {
            System.out.println("ATC: " + "The queue is currently full." + plane.getPlaneName() + ", please refer to the next nearest airport to avoid running out of fuel.");
            System.out.println("ATC: " + plane.getPlaneName() + " left the queue ...");
            return;
         }
         ((LinkedList<Plane>) planeQueue).offer(plane);
         System.out.println("ATC: " + plane.getPlaneName() + " successfully joined the waiting queue to land.");
         if (planeQueue.size() == 1) {
            planeQueue.notify();
         }
      }
   }

   void land(DockingGate gate, int gateNo) {
      Plane plane;
      synchronized (planeQueue) {
         while (planeQueue.size() == 0) {
            System.out.println("ATC: " + "Runway is empty. Waiting for planes.");
            try {
               planeQueue.wait();
            } catch (InterruptedException iex) {
               iex.printStackTrace();
            }
         }
         System.out.println("ATC: " + ((Plane) ((LinkedList<?>) planeQueue).peekFirst()).getPlaneName() + " is requesting to use the runway");
         plane = (Plane) ((LinkedList<?>) planeQueue).poll();
      }

      // if any gates are open, then allow to land
      long duration = 0;
      try {
         System.out.println("ATC: " + plane.getPlaneName() + " is currently using the runway.");
         duration = (long) (Math.random() * 10);
         TimeUnit.SECONDS.sleep(duration);
      } catch (InterruptedException iex) {
         iex.printStackTrace();
      }
      System.out.println("ATC: " + plane.getPlaneName() + " has completed using the runway in " + duration + " seconds.");
      System.out.println("");
      System.out.println("ATC: " + "RUNWAY IS NOW AVAILABLE");
      System.out.println("ATC: " + plane.getPlaneName() + " please dock at gate " + gateNo);
      dock(plane, gate, gateNo);
   }

   void dock(Plane plane, DockingGate gate, int gateNo) {
      gate.setOccupied(true);
      long duration = 5;
      try {
         System.out.println("ATC: " + plane.getPlaneName() + " is currently docking at gate " + gateNo);
         TimeUnit.SECONDS.sleep(duration);
         System.out.println("ATC: " + plane.getPlaneName() + " has successfully docked at gate " + gateNo + " ,and will now be unboarding passengers.");
      } catch (InterruptedException iex) {
         iex.printStackTrace();
      }

      System.out.println("");
   }

   void undock(Plane plane, DockingGate gate, int gateNo) {
      long duration = 5;
      try {
         System.out.println("ATC: " + plane.getPlaneName() + " is currently undocking from gate " + gateNo);
         TimeUnit.SECONDS.sleep(duration);
         System.out.println("ATC: " + plane.getPlaneName() + " has now left gate " + gateNo);
      } catch (InterruptedException iex) {
         iex.printStackTrace();
      }
      System.out.println("ATC: " + plane.getPlaneName() + " is now using the runway to take off.");
      System.out.println("ATC: " + plane.getPlaneName() + " has successfully departed.");
      System.out.println("");
   }

}
