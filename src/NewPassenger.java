
public class NewPassenger implements Runnable {

   Plane plane;
   int id;
   String passengerName;

   public NewPassenger(Plane plane) {
      this.plane = plane;
   }

   public Plane getPlane() {
      return plane;
   }

   public void setPlane(Plane plane) {
      this.plane = plane;
   }

   public int getId() {
      return id;
   }

   public void setId(int id) {
      this.id = id;
   }

   public String getPassengerName() {
      return passengerName;
   }

   public void setPassengerName(String passengerName) {
      this.passengerName = passengerName;
   }

   public void run() {
      System.out.println(passengerName + " is now boarding " + plane.planeName);
   }

}
