package UserInterface.WorkAreas.FacultyRole;

/**
 * Input checks shared by the Faculty screens, so every screen validates the
 * same way before anything is saved to the model.
 *
 * @author Samuel Haddad
 */
public class FacultyInputRules {

    private FacultyInputRules() {
        //static helper, never instantiated
    }

    //null-safe check for an empty or whitespace-only value
    public static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    //letters, spaces, hyphens and apostrophes only (e.g. Mary-Ann O'Neil)
    public static boolean isValidName(String s) {
        return !isBlank(s) && s.trim().length() <= 40 && s.trim().matches("[A-Za-z][A-Za-z '\\-]*");
    }

    //simple shape check: something@something.domain
    public static boolean isValidEmail(String s) {
        return !isBlank(s) && s.trim().matches("[\\w.+\\-]+@[\\w\\-]+(\\.[\\w\\-]+)+");
    }

    //phone is optional; when given it must have exactly 10 digits (spaces, dashes, dots and brackets are ignored)
    public static boolean isValidPhone(String s) {
        if (isBlank(s)) {
            return true;
        }
        String digits = s.replaceAll("[\\s\\-().]", "");
        return digits.matches("\\d{10}");
    }

    //optional free text with an upper limit on length
    public static boolean isWithinLength(String s, int max) {
        return s == null || s.trim().length() <= max;
    }

}
