package info5100.university.example.CourseSchedule;

import java.util.LinkedHashMap;

/**
 * Summarizes how one class performed: enrollment, how many students are
 * graded, the class average and the spread of letter grades. Built from the
 * seat assignments of the course offer, in the same spirit as the product
 * summary in the pricing model.
 *
 * @author Samuel Haddad
 */
public class CourseOfferSummary {

    CourseOffer courseoffer;
    int enrolled;      //students holding a seat
    int graded;        //students with a grade (grade > 0)
    float average;     //average grade points of graded students, 0 if none graded
    float highest;
    float lowest;
    LinkedHashMap<String, Integer> distribution; //letter grade -> number of students, best letter first

    public CourseOfferSummary(CourseOffer co) {
        courseoffer = co;
        distribution = new LinkedHashMap<>();
        for (String letter : GradeScale.LETTERS) {
            distribution.put(letter, 0);
        }
        distribution.put(GradeScale.NOT_GRADED, 0);

        float sum = 0;
        highest = 0;
        lowest = 0;
        for (Seat seat : co.getSeatList()) {
            SeatAssignment sa = seat.getSeatAssignment();
            if (!seat.isOccupied() || sa == null) {
                continue;
            }
            enrolled++;
            float g = sa.getGrade();
            String letter = GradeScale.toLetter(g);
            distribution.put(letter, distribution.get(letter) + 1);
            if (GradeScale.isGraded(g)) {
                graded++;
                sum = sum + g;
                if (graded == 1 || g > highest) {
                    highest = g;
                }
                if (graded == 1 || g < lowest) {
                    lowest = g;
                }
            }
        }
        average = graded == 0 ? 0 : sum / graded;
    }

    public CourseOffer getCourseOffer() {
        return courseoffer;
    }

    public int getEnrolled() {
        return enrolled;
    }

    public int getGraded() {
        return graded;
    }

    public float getAverage() {
        return average;
    }

    public float getHighest() {
        return highest;
    }

    public float getLowest() {
        return lowest;
    }

    public LinkedHashMap<String, Integer> getDistribution() {
        return distribution;
    }

    @Override
    public String toString() {
        return courseoffer.toString();
    }

}
