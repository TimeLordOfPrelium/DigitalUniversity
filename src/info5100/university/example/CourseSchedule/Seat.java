/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package info5100.university.example.CourseSchedule;

/**
 *
 * @author kal bugrara
 * @author Philip Dixon (modified)
 */
public class Seat {
    
    Boolean occupied; 
    int number;
    SeatAssignment seatassignment; //links back to studentprofile
    CourseOffer courseoffer;
    public Seat (CourseOffer co, int n){
        courseoffer = co;
        number = n;
        occupied = false;
        
    }
    
    public Boolean isOccupied(){
        return occupied;

    }
    public SeatAssignment newSeatAssignment(CourseLoad cl){
        
        seatassignment = new SeatAssignment(cl, this); //links seatassignment to seat
        occupied = true;   
        return seatassignment;
    }
    public CourseOffer getCourseOffer(){
        return courseoffer;
    }
    public int getCourseCredits(){
        return courseoffer.getCreditHours();
    }
    public SeatAssignment getSeatAssignment(){
        return seatassignment;
    }
    //frees the seat so another student can take it (used when a student drops or is deleted)
    public void release(){
        seatassignment = null;
        occupied = false;
    }
}
