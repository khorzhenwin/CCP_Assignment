
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Airport {

   int maxQueueSize = 6;
   List<Plane> planeQueue;

   public Airport() {
      planeQueue = new LinkedList<Plane>();
   }

   void joinQueue(Plane plane) {
      System.out.println("Thread-" + plane.getPlaneName() + " is requesting to enter the queue to land.");
      synchronized (planeQueue) {
         if (planeQueue.size() == maxQueueSize) {
            System.out.println("ATC: " + "The queue is currently full." + plane.getPlaneName()
                    + ", please refer to the next nearest airport to avoid running out of fuel.");
            System.out.println(plane.getPlaneName() + " left the queue ...");
            return;
         }
         ((LinkedList<Plane>) planeQueue).offer(plane);
         System.out.println(Main.ANSI_YELLOW + "Thread-" + plane.getPlaneName() + " successfully joined the waiting queue to land." + Main.ANSI_RESET);
         plane.setCurrentTime(System.currentTimeMillis());
         if (planeQueue.size() == 1) {
            planeQueue.notify();
         }
      }
   }

   Plane land(DockingGate gate, int gateNo) {
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
         System.out.println("Thread-" + ((Plane) ((LinkedList<?>) planeQueue).peekFirst()).getPlaneName()
                 + " is requesting to use the runway");
         plane = (Plane) ((LinkedList<?>) planeQueue).poll();
      }

      // if any gates are open, then allow to land
      long duration = 0;
      try {
         System.out.println("Thread-" + plane.getPlaneName() + " is currently using the runway.");
         duration = (long) (Math.random() * 10);
         Thread.sleep(duration);
      } catch (InterruptedException iex) {
         iex.printStackTrace();
      }
      System.out.println("Thread-" + plane.getPlaneName() + " has completed using the runway in " + duration + " seconds.");
      System.out.println("ATC: " + plane.getPlaneName() + " please dock at gate " + gateNo);
      System.out.println("");
      System.out.println("ATC: " + "RUNWAY IS NOW AVAILABLE");
      dock(plane, gate, gateNo);
      plane.setLandingTime(System.currentTimeMillis());
      return plane;
   }

   void depart(Plane localPlane, DockingGate gate, int gateNo) {
      long duration = 0;
      try {
         System.out.println(Main.ANSI_GREEN_BACKGROUND + Main.ANSI_BLUE + "Thread-" + localPlane.getPlaneName() + " is now departing from the airport." + Main.ANSI_RESET);
         duration = (long) (Math.random() * 5);
         TimeUnit.SECONDS.sleep(duration);
      } catch (InterruptedException iex) {
         iex.printStackTrace();
      }
      undock(localPlane, gate, gateNo);
   }

   void dock(Plane plane, DockingGate gate, int gateNo) {
      try {
         gate.dock();
      } catch (InterruptedException ex) {
         Logger.getLogger(Airport.class.getName()).log(Level.SEVERE, null, ex);
      }
      long duration = 5;
      try {
         System.out.println("ATC: " + plane.getPlaneName() + " is coasting to gate " + gateNo);
         TimeUnit.SECONDS.sleep(duration);
         System.out.println("Thread-" + plane.getPlaneName() + " has successfully docked at gate " + gateNo
                 + " ,and will now be unboarding passengers.");

      } catch (InterruptedException iex) {
         iex.printStackTrace();
      }

      System.out.println("");
   }

   void undock(Plane plane, DockingGate gate, int gateNo) {
      try {
         System.out.println(Main.ANSI_GREEN + "Thread-" + plane.getPlaneName() + " is currently undocking from gate " + gateNo + Main.ANSI_RESET);
         TimeUnit.SECONDS.sleep(2);
         System.out.println(Main.ANSI_GREEN + "Thread-" + plane.getPlaneName() + " has now left gate " + gateNo + Main.ANSI_RESET);
      } catch (InterruptedException iex) {
         iex.printStackTrace();
      }
      System.out.println(Main.ANSI_GREEN + "Thread-" + plane.getPlaneName() + " is now coasting to the runway to depart from " + gateNo + Main.ANSI_RESET);
      System.out.println(Main.ANSI_GREEN + "Thread-" + plane.getPlaneName() + " is now using the runway to take off." + Main.ANSI_RESET);
      System.out.println(Main.ANSI_GREEN + plane.getPlaneName() + " has successfully departed." + Main.ANSI_RESET);
      plane.setDepartureTime(System.currentTimeMillis());
      System.out.println("");
      gate.undock();
   }

   void refuel(Plane plane, DockingGate gate, int gateNo) {
      try {
         System.out.println(Main.ANSI_PURPLE_BACKGROUND + Main.ANSI_WHITE + "ATC: Refuelling truck is attending to " + plane.getPlaneName() + " at gate " + gateNo + Main.ANSI_RESET);
         System.out.println(Main.ANSI_PURPLE_BACKGROUND + Main.ANSI_WHITE + "ATC: Now refuelling " + plane.getPlaneName() + " at gate " + gateNo + Main.ANSI_RESET);
         TimeUnit.SECONDS.sleep(5);
         System.out.println(Main.ANSI_PURPLE_BACKGROUND + Main.ANSI_WHITE + "ATC: Refuelling completed for " + plane.getPlaneName() + " , refuelling truck is now leaving gate " + gateNo + Main.ANSI_RESET);
      } catch (InterruptedException iex) {
         iex.printStackTrace();
      }
      System.out.println(Main.ANSI_PURPLE_BACKGROUND + Main.ANSI_WHITE + "ATC: Refuelling truck is now available" + Main.ANSI_RESET);
   }

}
