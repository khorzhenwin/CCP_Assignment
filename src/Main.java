
public class Main {

   public static void main(String[] args) {
      Airport airport = new Airport();
      DockingGate[] gates = new DockingGate[2];
      gates[0] = new DockingGate(airport, 0) {
      };
      gates[1] = new DockingGate(airport, 1) {
      };
      Runway runway = new Runway(airport, gates[0], gates[1]);

      Thread runwayThread = new Thread(runway);
      runwayThread.start();

   }

}
