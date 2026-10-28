package info5100.university.example.CourseSchedule;

/**
 * Converts between letter grades and grade points (A=4.0, A-=3.7, B+=3.3, ...).
 * A grade of 0 means the class has not been graded yet, which is how the rest
 * of the model (e.g. Transcript.getGPA) treats it.
 *
 * @author Samuel Haddad
 */
public class GradeScale {

    public static final String NOT_GRADED = "Not graded";

    //letters faculty can assign, best first
    public static final String[] LETTERS = {"A", "A-", "B+", "B", "B-", "C+", "C", "C-", "D"};
    static final float[] POINTS = {4.0f, 3.7f, 3.3f, 3.0f, 2.7f, 2.3f, 2.0f, 1.7f, 1.0f};

    private GradeScale() {
        //static helper, never instantiated
    }

    //grade points for a letter, or -1 if the letter is not on the scale
    public static float toPoints(String letter) {
        if (letter == null) {
            return -1;
        }
        for (int i = 0; i < LETTERS.length; i++) {
            if (LETTERS[i].equalsIgnoreCase(letter.trim())) {
                return POINTS[i];
            }
        }
        return -1;
    }

    //closest letter for any grade point value (also works for averages like 3.45)
    public static String toLetter(float points) {
        if (points <= 0) {
            return NOT_GRADED;
        }
        for (int i = 0; i < POINTS.length - 1; i++) {
            //halfway between this letter and the next one down
            float cutoff = (POINTS[i] + POINTS[i + 1]) / 2;
            if (points >= cutoff) {
                return LETTERS[i];
            }
        }
        return LETTERS[LETTERS.length - 1];
    }

    public static boolean isGraded(float points) {
        return points > 0;
    }

}
