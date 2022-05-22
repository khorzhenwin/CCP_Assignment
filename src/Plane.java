
import java.util.LinkedList;
import java.util.concurrent.locks.Lock;

public class Plane implements Runnable {

   Airport airport;
   Runway runway;
   Lock lock;
   int id;

   String planeName;
   boolean landed, inQueue = false;

   public Plane(Airport airport) {
      this.airport = airport;
   }

   public Runway getRunway() {
      return runway;
   }

   public void setRunway(Runway runway) {
      this.runway = runway;
   }

   public Lock getLock() {
      return lock;
   }

   public void setLock(Lock lock) {
      this.lock = lock;
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

   public boolean isLanded() {
      return landed;
   }

   public void setLanded(boolean landed) {
      this.landed = landed;
   }

   public boolean isInQueue() {
      return inQueue;
   }

   public void setInQueue(boolean inQueue) {
      this.inQueue = inQueue;
   }

   @Override
   public void run() {

      airport.joinQueue(this);
      // lock.unlock();

   }

}
