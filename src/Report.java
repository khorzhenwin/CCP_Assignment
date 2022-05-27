
public class Report {

   private String planeName;
   private int passengerInCount, passengerOutCount;
   private double landingWaitingTime;

   public Report(String planeName, int passengerInCount, int passengerOutCount, double landingWaitingTime) {
      this.planeName = planeName;
      this.passengerInCount = passengerInCount;
      this.passengerOutCount = passengerOutCount;
      this.landingWaitingTime = landingWaitingTime;
   }

   public String getPlaneName() {
      return planeName;
   }

   public void setPlaneName(String planeName) {
      this.planeName = planeName;
   }

   public int getPassengerInCount() {
      return passengerInCount;
   }

   public void setPassengerInCount(int passengerInCount) {
      this.passengerInCount = passengerInCount;
   }

   public int getPassengerOutCount() {
      return passengerOutCount;
   }

   public void setPassengerOutCount(int passengerOutCount) {
      this.passengerOutCount = passengerOutCount;
   }

   public double getLandingWaitingTime() {
      return landingWaitingTime;
   }

   public void setLandingWaitingTime(double landingWaitingTime) {
      this.landingWaitingTime = landingWaitingTime;
   }

}
