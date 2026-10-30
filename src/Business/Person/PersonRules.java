/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Business.Person;

/**
 * Validation rules for a person's details, shared by every screen that registers
 * or edits a person. Returns an error message, or null when the details are valid.
 * Callers pass trimmed strings.
 *
 * @author Philip Dixon
 */
public class PersonRules {

    private PersonRules() {
        //static helper, never instantiated
    }

    //except: the person being edited (they may keep their own email); null when registering
    public static String validate(PersonDirectory pd, String first, String last, String email, String phone, Person except) {

        if (first.isEmpty() || last.isEmpty()) {
            return "Please enter both a first and a last name.";
        }
        if (!first.matches("[\\p{L} .'-]{1,40}") || !last.matches("[\\p{L} .'-]{1,40}")) {
            return "Names may only contain letters, spaces, '.', apostrophes and '-'.";
        }
        if (email.isEmpty()) {
            return "Please enter an email address.";
        }
        if (!email.matches("[^@\\s]+@[^@\\s]+\\.[^@\\s]+")) {
            return "Please enter a valid email address.";
        }
        Person existing = pd.findPersonByEmail(email);
        if (existing != null && existing != except) {
            return "The email " + email + " is already registered to " + existing.getFullName() + ".";
        }
        if (!phone.isEmpty() && !phone.matches("[0-9 ()+-]{7,20}")) {
            return "Phone numbers may contain 7-20 digits, spaces, '+', '-', '(' or ')'.";
        }
        return null;
    }

}
