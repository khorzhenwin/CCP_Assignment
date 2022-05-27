
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
      System.out.println("All passengers have successfully disembarked from " + plane.getPlaneName());
      System.out.println("ATC: Passengers may now begin boarding " + plane.getPlaneName());
      for (int i = 1; i < 51; i++) {
         NewPassenger newPassenger = new NewPassenger(plane);
         Thread passengerThread = new Thread(newPassenger);
         newPassenger.setPassengerName("Passenger " + passengerThread.getId());
         passengerThread.start();
         try {
            TimeUnit.MILLISECONDS.sleep(100);
         } catch (InterruptedException iex) {
            iex.printStackTrace();
         }
      }
      System.out.println("All passengers have successfully boarded " + plane.getPlaneName());
      return;
   }

}