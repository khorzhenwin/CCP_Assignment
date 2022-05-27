
public class Plane implements Runnable {

   Airport airport;
   int id;
   String planeName;
   long currentTime, landingTime;
   int passengerOutCount, passengerInCount;

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

   public long getCurrentTime() {
      return currentTime;
   }

   public void setCurrentTime(long currentTime) {
      this.currentTime = currentTime;
   }

   public long getLandingTime() {
      return landingTime;
   }

   public void setLandingTime(long landingTime) {
      this.landingTime = landingTime;
   }

   public int getPassengerOutCount() {
      return passengerOutCount;
   }

   public void setPassengerOutCount(int passengerOutCount) {
      this.passengerOutCount = passengerOutCount;
   }

   public int getPassengerInCount() {
      return passengerInCount;
   }

   public void setPassengerInCount(int passengerInCount) {
      this.passengerInCount = passengerInCount;
   }

   @Override
   public void run() {

      airport.joinQueue(this);

   }

}
