
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class PassengerGenerator implements Runnable {

   Plane plane;
   int randomNum1 = ThreadLocalRandom.current().nextInt(30, 51);
   int randomNum2 = ThreadLocalRandom.current().nextInt(30, 51);

   public PassengerGenerator(Plane plane) {
      this.plane = plane;
   }

   public void run() {
      plane.setPassengerOutCount(randomNum1);
      plane.setPassengerInCount(randomNum2);
      for (int i = 1; i < randomNum1; i++) {
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
      System.out.println(Main.ANSI_CYAN + "All " + randomNum1 + " passengers have successfully disembarked from " + plane.getPlaneName() + Main.ANSI_RESET);
      System.out.println(Main.ANSI_CYAN + "ATC: Passengers may now begin boarding " + plane.getPlaneName());
      for (int i = 1; i < randomNum2; i++) {
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
      System.out.println(Main.ANSI_CYAN + "All " + randomNum2 + " passengers have successfully boarded " + plane.getPlaneName() + Main.ANSI_RESET);
      System.out.println(Main.ANSI_CYAN + plane.getPlaneName() + " is waiting for clearance from ATC for departure" + Main.ANSI_RESET);
      return;
   }

}
