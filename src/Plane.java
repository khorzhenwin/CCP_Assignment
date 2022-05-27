
public class Plane implements Runnable {

   Airport airport;
   int id;
   String planeName;

   public Plane(Airport airport) {
      this.airport = airport;
   }

   public int getId() {
      return id;
   }

   public void setId(int id) {
      this.id = id;
   }

   public String getPlaneName() {
      return planeName;
   }

   public void setPlaneName(String planeName) {
      this.planeName = planeName;
   }

   public Airport getAirport() {
      return airport;
   }

   public void setAirport(Airport airport) {
      this.airport = airport;
   }

   @Override
   public void run() {

      airport.joinQueue(this);

   }

}
