
public class Report {

   private String planeName;
   private int passengerInCount, passengerOutCount;
   private long landingWaitingTime, totalTime;

   public Report(String planeName, int passengerInCount, int passengerOutCount, long landingWaitingTime, long totalTime) {
      this.planeName = planeName;
      this.passengerInCount = passengerInCount;
      this.passengerOutCount = passengerOutCount;
      this.landingWaitingTime = landingWaitingTime;
      this.totalTime = totalTime;
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

   public long getLandingWaitingTime() {
      return landingWaitingTime;
   }

   public void setLandingWaitingTime(int landingWaitingTime) {
      this.landingWaitingTime = landingWaitingTime;
   }

   public long getTotalTime() {
      return totalTime;
   }

   public void setTotalTime(int totalTime) {
      this.totalTime = totalTime;
   }

}
