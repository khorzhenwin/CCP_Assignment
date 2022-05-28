
import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

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
               if (airport.planeQueue.size() > 0 && ((!gate1.isOccupied() && gate2.isOccupied()) || (!gate1.isOccupied() && !gate2.isOccupied()))) {
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
                  // clearing gates when no more planes are coming in
                  if (!planeThread.isAlive() && (gate1.isOccupied() || gate2.isOccupied())) {
                     takeOffPriority = true;
                     notifyAll();
                  }
               } else if (airport.planeQueue.size() > 0 && (gate1.isOccupied() && !gate2.isOccupied())) {
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
                  // clearing gates when no more planes are coming in
                  if (!planeThread.isAlive() && (gate1.isOccupied() || gate2.isOccupied())) {
                     takeOffPriority = true;
                     notifyAll();
                  }
               } else if (airport.planeQueue.size() == 0) {
                  takeOffPriority = true;
                  run = false;
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
               } else if (airport.planeQueue.size() == 0) {
                  run = false;
               }
            } catch (Exception e) {
               System.out.println("Exception:" + e);
            }
         }
         // sanity check
         if (airport.planeQueue.size() == 0 && !gate1.isOccupied() && !gate2.isOccupied()) {
            System.out.println(ANSI_PURPLE + " Both gates are empty. Waiting for more planes ..." + ANSI_RESET);
            try {
               TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException ex) {
               Logger.getLogger(Runway.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println(ANSI_PURPLE + " There are no more planes for now" + ANSI_RESET);
            System.out.println(ANSI_PURPLE + " Printing out summary statistics ..." + ANSI_RESET);
            run = false;
            break;
         }
      }
      long maxWait = 0, avgWait = 0, minWait = 999999;
      long maxGroundTime = 0, avgGroundTime = 0, minGroundTime = 999999;
      for (int i = 0; i < allReports.size(); i++) {
         System.out.println("----------------------------------------------------------------------------------------------------------");
         System.out.println("Plane : " + allReports.get(i).getPlaneName());
         System.out.println("Total Passengers Disembarked : " + allReports.get(i).getPassengerOutCount());
         System.out.println("Total Passengers Embarked : " + allReports.get(i).getPassengerInCount());
         System.out.println("Total Waiting Time To Land : " + allReports.get(i).getLandingWaitingTime() + " ms");
         System.out.println("Total Time Spent At Airport : " + allReports.get(i).getTotalTime() + " ms");
         System.out.println("----------------------------------------------------------------------------------------------------------");
         maxWait = (maxWait < allReports.get(i).getLandingWaitingTime()) ? allReports.get(i).getLandingWaitingTime() : maxWait;
         minWait = (allReports.get(i).getLandingWaitingTime() < minWait) ? allReports.get(i).getLandingWaitingTime() : minWait;
         avgWait += allReports.get(i).getLandingWaitingTime();
         maxGroundTime = (maxGroundTime < allReports.get(i).getTotalTime()) ? allReports.get(i).getTotalTime() : maxGroundTime;
         minGroundTime = (allReports.get(i).getTotalTime() < minGroundTime) ? allReports.get(i).getTotalTime() : minGroundTime;
         avgGroundTime += allReports.get(i).getTotalTime();
      }
      System.out.println(ANSI_PURPLE + "Minimum Wait Time: " + minWait + ANSI_RESET);
      System.out.println(ANSI_PURPLE + "Maximum Wait Time: " + maxWait + ANSI_RESET);
      System.out.println(ANSI_PURPLE + "Average Wait Time: " + avgWait / allReports.size() + ANSI_RESET);
      System.out.println(ANSI_CYAN + "----------------------------------------------------------------------------------------" + ANSI_RESET);
      System.out.println(ANSI_PURPLE + "Minimum Ground Time: " + minGroundTime + ANSI_RESET);
      System.out.println(ANSI_PURPLE + "Maximum Ground Time: " + maxGroundTime + ANSI_RESET);
      System.out.println(ANSI_PURPLE + "Average Ground Time: " + avgGroundTime / allReports.size() + ANSI_RESET);
   }

   public synchronized void setTakeOffPriority() {
      takeOffPriority = true;
      System.out.println(ANSI_RED_BACKGROUND + ANSI_WHITE + "ATC: Blocking the runway until a plane is ready to leave." + ANSI_RESET);
   }
}
