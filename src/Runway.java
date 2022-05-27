
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

   Plane planeAtGate1, planeAtGate2;
   Airport airport;
   DockingGate gate1, gate2;
   public volatile boolean takeOffPriority = false;
   // 1 - plane left gate 1 recently
   // 2 - plane left gate 2 recently
   public volatile int lastToTakeOff = 0;
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
      System.out.println("ATC: Runway is now open");
      while (run) {
         while (!takeOffPriority) {
            try {
               // if both gates are free, gate 1 will be used as 1st choice
               if ((!gate1.isOccupied() && gate2.isOccupied()) || (!gate1.isOccupied() && !gate2.isOccupied())) {
                  System.out.println("ATC: Gate 1 is available");
                  planeAtGate1 = airport.land(gate1, 1);
                  notifyAll();
                  PassengerGenerator passengerGen = new PassengerGenerator(planeAtGate1);
                  Thread passengerThread = new Thread(passengerGen);
                  passengerThread.start();
               } else if (gate1.isOccupied() && !gate2.isOccupied()) {
                  System.out.println("ATC: Gate 2 is available");
                  planeAtGate2 = airport.land(gate2, 2);
                  notifyAll();
                  PassengerGenerator passengerGen = new PassengerGenerator(planeAtGate2);
                  Thread passengerThread = new Thread(passengerGen);
                  passengerThread.start();
               } else {
                  System.out.println("ATC: All docking gates are currently occupied. Please wait until a plane departs!");
                  setTakeOffPriority();
                  notifyAll();
               }
            } catch (Exception e) {
               e.printStackTrace();
            }
         }
         while (takeOffPriority) {
            // somehow try and make this block wait for passengerThread to finish running
            try {
               Thread.sleep(5000);
               if (gate1.isOccupied() && (lastToTakeOff == 2 || lastToTakeOff == 0)) {
                  airport.depart(planeAtGate1, gate1, 1);
                  takeOffPriority = false;
                  lastToTakeOff = 1;
                  notifyAll();
               } else if (gate2.isOccupied() && lastToTakeOff == 1) {
                  airport.depart(planeAtGate2, gate2, 2);
                  takeOffPriority = false;
                  lastToTakeOff = 2;
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
