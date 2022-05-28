
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class Runway implements Runnable {

   public static final String ANSI_RESET = "\u001B[0m";
   public static final String ANSI_BLACK = "\u001B[30m";
   public static final String ANSI_RED = "\u001B[31m";
   public static final String ANSI_GREEN = "\u001B[32m";
   public static final String ANSI_YELLOW = "\u001B[33m";
   public static final String ANSI_BLUE = "\u001B[34m";
   public static final String ANSI_PURPLE = "\u001B[35m";
   public static final String ANSI_CYAN = "\u001B[36m";
   public static final String ANSI_WHITE = "\u001B[37m";
   public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
   public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
   public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
   public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
   public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
   public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
   public static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
   public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";

   public static ArrayList<Report> allReports = new ArrayList<Report>();

   Plane planeAtGate1, planeAtGate2;
   Airport airport;
   DockingGate gate1, gate2;
   public volatile boolean passengerThread1Completed, passengerThread2Completed = false;
   public volatile boolean takeOffPriority = false;
   public volatile int lastToTakeOff = 0;
   public volatile boolean run = true;

   public Runway(Airport ATC, DockingGate gate1, DockingGate gate2) {
      this.airport = ATC;
      this.gate1 = gate1;
      this.gate2 = gate2;
   }

   public synchronized void run() {
      PlaneGenerator planeGen = new PlaneGenerator(airport);
      Thread planeThread = new Thread(planeGen);
      planeThread.start();
      Semaphore sm = new Semaphore(1);
      try {
         Thread.sleep(1000);
      } catch (InterruptedException iex) {
         iex.printStackTrace();
      }
      System.out.println("ATC: Runway is now open");
      while (run) {
         while (!takeOffPriority) {
            try {
               // if both gates are free, gate 1 will be used as 1st choice
               if ((!gate1.isOccupied() && gate2.isOccupied()) || (!gate1.isOccupied() && !gate2.isOccupied())) {
                  System.out.println("ATC: Gate 1 is available");
                  planeAtGate1 = airport.land(gate1, 1);
                  notifyAll();
                  PassengerGenerator passengerGen = new PassengerGenerator(planeAtGate1);
                  Cleaner cleaner = new Cleaner(planeAtGate1);
                  Thread passengerThread = new Thread(passengerGen);
                  Thread cleanerThread = new Thread(cleaner);
                  passengerThread.start();
                  cleanerThread.start();
                  sm.acquire();
                  airport.refuel(planeAtGate1, gate1, 1);
                  passengerThread1Completed = true;
                  sm.release();
                  notifyAll();
               } else if (gate1.isOccupied() && !gate2.isOccupied()) {
                  System.out.println("ATC: Gate 2 is available");
                  planeAtGate2 = airport.land(gate2, 2);
                  notifyAll();
                  PassengerGenerator passengerGen = new PassengerGenerator(planeAtGate2);
                  Cleaner cleaner = new Cleaner(planeAtGate2);
                  Thread passengerThread = new Thread(passengerGen);
                  Thread cleanerThread = new Thread(cleaner);
                  passengerThread.start();
                  cleanerThread.start();
                  sm.acquire();
                  airport.refuel(planeAtGate2, gate2, 2);
                  passengerThread2Completed = true;
                  sm.release();
                  notifyAll();
               } else {
                  System.out.println("ATC: All docking gates are currently occupied. Please wait until a plane departs!");
                  setTakeOffPriority();
                  notifyAll();
               }
            } catch (Exception e) {
               e.printStackTrace();
            }
         }
         while (takeOffPriority) {
            // somehow try and make this block run when passengerThread finish running
            try {
               if (gate1.isOccupied() && (lastToTakeOff == 2 || lastToTakeOff == 0) && passengerThread1Completed) {
                  airport.depart(planeAtGate1, gate1, 1);
                  takeOffPriority = false;
                  lastToTakeOff = 1;
                  Report newReport = new Report(planeAtGate1.getPlaneName(),
                          planeAtGate1.getPassengerInCount(),
                          planeAtGate1.passengerOutCount,
                          planeAtGate1.getLandingTime() - planeAtGate1.getCurrentTime(),
                          planeAtGate1.getDepartureTime() - planeAtGate1.getLandingTime());
                  allReports.add(newReport);
                  notifyAll();
               } else if (gate2.isOccupied() && lastToTakeOff == 1 && passengerThread2Completed) {
                  airport.depart(planeAtGate2, gate2, 2);
                  takeOffPriority = false;
                  lastToTakeOff = 2;
                  Report newReport = new Report(planeAtGate2.getPlaneName(),
                          planeAtGate2.getPassengerInCount(),
                          planeAtGate2.passengerOutCount,
                          planeAtGate2.getLandingTime() - planeAtGate2.getCurrentTime(),
                          planeAtGate2.getDepartureTime() - planeAtGate2.getLandingTime());
                  allReports.add(newReport);
                  notifyAll();
               }
            } catch (Exception e) {
               System.out.println("Exception:" + e);
            }
         }
         if (allReports.size() == 6) {
            run = false;
            planeGen.setClosing();
         }
      }
      for (int i = 0; i < allReports.size(); i++) {
         System.out.println("----------------------------------------------------------------------------------------------------------");
         System.out.println("Plane : " + allReports.get(i).getPlaneName());
         System.out.println("Total Passengers Disembarked : " + allReports.get(i).getPassengerOutCount());
         System.out.println("Total Passengers Embarked : " + allReports.get(i).getPassengerInCount());
         System.out.println("Total Waiting Time To Land : " + allReports.get(i).getLandingWaitingTime() + " ms");
         System.out.println("Total Time Spent At Airport : " + allReports.get(i).getTotalTime() + " ms");
         System.out.println("----------------------------------------------------------------------------------------------------------");
      }
   }

   public synchronized void setTakeOffPriority() {
      takeOffPriority = true;
      System.out.println(ANSI_RED_BACKGROUND + ANSI_WHITE + "ATC: Blocking the runway until a plane is ready to leave." + ANSI_RESET);
   }
}
