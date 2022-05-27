
import java.util.concurrent.TimeUnit;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author user
 */
public class PassengerGenerator implements Runnable {

   Plane plane;

   public PassengerGenerator(Plane plane) {
      this.plane = plane;
   }

   public void run() {
      for (int i = 1; i < 51; i++) {
         Passenger passenger = new Passenger(plane);
         Thread passengerThread = new Thread(passenger);
         passenger.setPassengerName("Passenger " + passengerThread.getId());
         passengerThread.start();

         try {
            TimeUnit.MILLISECONDS.sleep(100);
         } catch (InterruptedException iex) {
            iex.printStackTrace();
         }
      }
      return;
   }

}
