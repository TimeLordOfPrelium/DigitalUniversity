package Business.Faculty;

import info5100.university.example.CourseSchedule.SeatAssignment;
import info5100.university.example.Persona.Transcript;

import java.util.ArrayList;

/**
 * Academic progress numbers faculty see on a student's profile, worked out
 * from the student's transcript. Classes without a grade yet count as in progress.
 *
 * @author Samuel Haddad
 */
public class StudentProgress {

    public static final int CREDITS_TO_GRADUATE = 32; //MSIS requirement

    private StudentProgress() {
        //static helper, never instantiated
    }

    //classes that already have a grade
    public static ArrayList<SeatAssignment> getCompletedClasses(Transcript transcript) {
        ArrayList<SeatAssignment> completed = new ArrayList<>();
        for (SeatAssignment sa : transcript.getCourseList()) {
            if (sa.getGrade() > 0) {
                completed.add(sa);
            }
        }
        return completed;
    }

    public static int getCompletedCredits(Transcript transcript) {
        int credits = 0;
        for (SeatAssignment sa : getCompletedClasses(transcript)) {
            credits = credits + sa.getCreditHours();
        }
        return credits;
    }

    //every class on the transcript, oldest semester first
    public static ArrayList<SeatAssignment> getClassesBySemester(Transcript transcript) {
        ArrayList<SeatAssignment> classes = new ArrayList<>(transcript.getCourseList());
        classes.sort((a, b) -> a.getCourseLoad().getSemester().compareTo(b.getCourseLoad().getSemester()));
        return classes;
    }

}
