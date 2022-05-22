
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

   Airport ATC;
   DockingGate gate1, gate2;
   public boolean takeOffPriority = false;

   public Runway(Airport ATC, DockingGate gate1, DockingGate gate2) {
      this.ATC = ATC;
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
         if (gate1.isOccupied() == false) {
            gate1.setOccupied(true);
            ATC.land();
            System.out.println("Please dock at gate 1");
            notifyAll();
         } else if (gate2.isOccupied() == false && gate1.isOccupied()) {
            gate2.setOccupied(true);
            ATC.land();
            System.out.println("Please dock at gate 2");
            notifyAll();
         } else {
            try {
               wait();
            } catch (InterruptedException ex) {
               Logger.getLogger(Runway.class.getName()).log(Level.SEVERE, null, ex);
            }
         };

      }
   }

}
