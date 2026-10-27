package Business.Faculty;

import Business.Business;
import Business.Profiles.FacultyProfile;
import Business.Profiles.StudentProfile;

import info5100.university.example.CourseSchedule.CourseOffer;
import info5100.university.example.CourseSchedule.CourseSchedule;
import info5100.university.example.CourseSchedule.Seat;
import info5100.university.example.CourseSchedule.SeatAssignment;
import info5100.university.example.Persona.Faculty.FacultyAssignment;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Answers the questions every Faculty screen needs: which classes does this
 * faculty member teach, who is enrolled in them, and is a given student one of
 * their students. Faculty screens only ever show data that comes through here,
 * so a faculty member can never reach classes or students that are not theirs.
 *
 * @author Samuel Haddad
 */
public class TeachingRoster {

    private TeachingRoster() {
        //static helper, never instantiated
    }

    //the semester a course offer belongs to (course offers do not store it themselves)
    public static String getSemester(Business business, CourseOffer co) {
        for (CourseSchedule cs : business.getDepartment().getCourseSchedules()) {
            if (cs.getSchedule().contains(co)) {
                return cs.getSemester();
            }
        }
        return "";
    }

    //only the current semester can still be changed; older semesters are closed
    public static boolean isCurrentSemester(Business business, CourseOffer co) {
        String current = business.getCurrentSemester();
        return current != null && current.equals(getSemester(business, co));
    }

    //every class the faculty member teaches; pass null for all semesters
    public static ArrayList<CourseOffer> getCourseOffers(Business business, FacultyProfile faculty, String semester) {
        ArrayList<CourseOffer> offers = new ArrayList<>();
        for (FacultyAssignment fa : faculty.getAcademicProfile().getFacultyAssignments()) {
            CourseOffer co = fa.getCourseOffer();
            if (semester == null || semester.equals(getSemester(business, co))) {
                offers.add(co);
            }
        }
        //newest semester first, then by course number
        offers.sort((a, b) -> {
            int bysemester = getSemester(business, b).compareTo(getSemester(business, a));
            return bysemester != 0 ? bysemester : a.getCourseNumber().compareTo(b.getCourseNumber());
        });
        return offers;
    }

    //the semesters this faculty member has taught in, newest first
    public static ArrayList<String> getSemesters(Business business, FacultyProfile faculty) {
        ArrayList<String> semesters = new ArrayList<>();
        for (CourseOffer co : getCourseOffers(business, faculty, null)) {
            String s = getSemester(business, co);
            if (!semesters.contains(s)) {
                semesters.add(s);
            }
        }
        Collections.sort(semesters, Collections.reverseOrder());
        return semesters;
    }

    //the seat assignments (one per enrolled student) of a class
    public static ArrayList<SeatAssignment> getEnrolledSeats(CourseOffer co) {
        ArrayList<SeatAssignment> seats = new ArrayList<>();
        for (Seat seat : co.getSeatList()) {
            if (seat.isOccupied() && seat.getSeatAssignment() != null) {
                seats.add(seat.getSeatAssignment());
            }
        }
        return seats;
    }

    //the student who holds a seat assignment, or null if no student matches
    public static StudentProfile findStudent(Business business, SeatAssignment sa) {
        for (StudentProfile sp : business.getStudentDirectory().getStudentList()) {
            if (sp.getAcademicProfile().getTranscript().getCourseLoads().contains(sa.getCourseLoad())) {
                return sp;
            }
        }
        return null;
    }

    //students enrolled in one class, or (co == null) in any class this faculty member teaches
    public static ArrayList<StudentProfile> getStudents(Business business, FacultyProfile faculty, CourseOffer co) {
        ArrayList<CourseOffer> offers = new ArrayList<>();
        if (co != null) {
            offers.add(co);
        } else {
            offers = getCourseOffers(business, faculty, null);
        }
        ArrayList<StudentProfile> students = new ArrayList<>();
        for (CourseOffer offer : offers) {
            for (SeatAssignment sa : getEnrolledSeats(offer)) {
                StudentProfile sp = findStudent(business, sa);
                if (sp != null && !students.contains(sp)) {
                    students.add(sp);
                }
            }
        }
        students.sort((a, b) -> a.getPerson().getFullName().compareToIgnoreCase(b.getPerson().getFullName()));
        return students;
    }

    //true when the student is (or was) enrolled in one of this faculty member's classes
    public static boolean teaches(Business business, FacultyProfile faculty, StudentProfile student) {
        return getStudents(business, faculty, null).contains(student);
    }

}
