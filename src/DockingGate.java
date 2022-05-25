
abstract public class DockingGate {

   private boolean occupied = false;
   private Airport ATC;
   private int id;

   public DockingGate(Airport ATC, int id) {
      this.ATC = ATC;
      this.id = id;
   }

   public boolean isOccupied() {
      return occupied;
   }

   public void setOccupied(boolean occupied) {
      this.occupied = occupied;
   }

   public Airport getATC() {
      return ATC;
   }

   public void setATC(Airport ATC) {
      this.ATC = ATC;
   }

   public int getId() {
      return id;
   }

   public void setId(int id) {
      this.id = id;
   }

   public synchronized void undock() {
      occupied = false;
      notifyAll();
   }

   public synchronized void dock()
           throws java.lang.InterruptedException {
      while (occupied) {
         wait();
      }
      occupied = true;
   }
}
