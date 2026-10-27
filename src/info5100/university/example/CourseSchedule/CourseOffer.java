/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package info5100.university.example.CourseSchedule;

import info5100.university.example.CourseCatalog.Course;
import info5100.university.example.Persona.Faculty.FacultyAssignment;
import info5100.university.example.Persona.Faculty.FacultyProfile;
import java.util.ArrayList;

/**
 *
 * @author kal bugrara
 */
public class CourseOffer {

    Course course;
    ArrayList<Seat> seatlist;
    FacultyAssignment facultyassignment;

    // Faculty use case (Samuel Haddad): details the teaching faculty member manages
    String room;         //where the class meets
    String meetingtime;  //e.g. Mon/Wed 10:00-11:40 AM
    String description;  //short course offer description / syllabus notes

    public CourseOffer(Course c) {
        course = c;
        seatlist = new ArrayList();
        room = "";
        meetingtime = "";
        description = "";
    }
     
    public void AssignAsTeacher(FacultyProfile fp) {

        unassignTeacher();
        facultyassignment = fp.AssignAsTeacher(this); //keeps the faculty side of the link in sync
    }

    public void unassignTeacher() {
        if (facultyassignment == null) {
            return;
        }
        facultyassignment.getFacultyProfile().removeAssignment(facultyassignment);
        facultyassignment = null;
    }

    public FacultyProfile getFacultyProfile() {
        if (facultyassignment == null) {
            return null; //no teacher assigned yet
        }
        return facultyassignment.getFacultyProfile();
    }

    public String getCourseNumber() {
        return course.getCOurseNumber();
    }

    public void generatSeats(int n) {

        for (int i = 0; i < n; i++) {

            seatlist.add(new Seat(this, i));

        }

    }

    public Seat getEmptySeat() {

        for (Seat s : seatlist) {

            if (!s.isOccupied()) {
                return s;
            }
        }
        return null;
    }


    public SeatAssignment assignEmptySeat(CourseLoad cl) {

        Seat seat = getEmptySeat();
        if (seat == null) {
            return null;
        }
        SeatAssignment sa = seat.newSeatAssignment(cl); //seat is already linked to course offer
        cl.registerStudent(sa); //coures offer seat is now linked to student
        return sa;
    }

    public int getTotalCourseRevenues() {

        int sum = 0;

        for (Seat s : seatlist) {
            if (s.isOccupied() == true) {
                sum = sum + course.getCoursePrice();
            }

        }
        return sum;
    }
    public Course getSubjectCourse(){
        return course;
    }
    public int getCreditHours(){
        return course.getCredits();
    }

    public ArrayList<Seat> getSeatList(){
        return seatlist;
    }

    public int getEnrolledCount(){
        int count = 0;
        for (Seat s : seatlist) {
            if (s.isOccupied()) {
                count++;
            }
        }
        return count;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getMeetingTime() {
        return meetingtime;
    }

    public void setMeetingTime(String meetingtime) {
        this.meetingtime = meetingtime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    //total number of seats, taken or free
    public int getCapacity() {
        return seatlist.size();
    }

    //grows or shrinks the class; only empty seats are removed, so it can never drop below the enrollment
    public boolean setCapacity(int n) {
        if (n < getEnrolledCount() || n < 1) {
            return false;
        }
        while (seatlist.size() < n) {
            seatlist.add(new Seat(this, seatlist.size()));
        }
        for (int i = seatlist.size() - 1; i >= 0 && seatlist.size() > n; i--) {
            if (!seatlist.get(i).isOccupied()) {
                seatlist.remove(i);
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return course.getCOurseNumber() + " " + course.getName();
    }
}
