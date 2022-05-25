
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
   public volatile boolean takeOffPriority = false;
   boolean run = true;

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
      while (run) {
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
                  setTakeOffPriority();
                  notifyAll();
               }
            } catch (Exception e) {
               e.printStackTrace();
            }
         }
         while (takeOffPriority) {
            try {
               Thread.sleep(5000);
               if (gate1.isOccupied()) {
                  airport.depart(gate1, 1);
                  takeOffPriority = false;
                  notifyAll();
               } else if (gate2.isOccupied()) {
                  airport.depart(gate2, 2);
                  takeOffPriority = false;
                  notifyAll();
               }
            } catch (Exception e) {
               System.out.println("Exception:" + e);
            }
         }
      }
   }

   public synchronized void setTakeOffPriority() {
      takeOffPriority = true;
      System.out.println("Blocking the runway until a plane is ready to leave.");
   }
}
