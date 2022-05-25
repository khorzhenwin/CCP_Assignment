
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author user
 */
public class Runway implements Runnable {

   Airport airport;
   DockingGate gate1, gate2;
   public boolean takeOffPriority = false;

   public Runway(Airport ATC, DockingGate gate1, DockingGate gate2) {
      this.airport = ATC;
      this.gate1 = gate1;
      this.gate2 = gate2;
   }

   public synchronized void run() {
      try {
         Thread.sleep(1000);
      } catch (InterruptedException iex) {
         iex.printStackTrace();
      }
      System.out.println("Runway is now open");
      while (!takeOffPriority) {
         try {
            // if both gates are free, gate 1 will be used as 1st choice
            if ((!gate1.isOccupied() && gate2.isOccupied()) || (!gate1.isOccupied() && !gate2.isOccupied())) {
               System.out.println("Gate 1 is available");
               airport.land(gate1, 1);
               notifyAll();
            } else if (gate1.isOccupied() && !gate2.isOccupied()) {
               System.out.println("Gate 2 is available");
               airport.land(gate2, 2);
               notifyAll();
            } else {
               System.out.println("All docking gates are currently occupied. Please wait momentarily to land on the runway until a plane departs!");
               wait();
            }
         } catch (Exception e) {
            e.printStackTrace();
         }

      }
   }

   public synchronized void setTakeOffPriority() {
      takeOffPriority = true;
      System.out.println("Plane is trying to depart. Please clear the runway!");
   }
}
