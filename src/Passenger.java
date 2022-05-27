/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author user
 */
public class Passenger implements Runnable {

   Plane plane;
   int id;
   String passengerName;

   public Passenger(Plane plane) {
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
      System.out.println(passengerName + " is now leaving " + plane.planeName);
   }

}
