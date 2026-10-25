/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Business.UserAccounts;

/**
 * Validation rules for user names and passwords, shared by every screen that
 * creates or edits an account. Each check returns an error message, or null when valid.
 */
public class AccountRules {

    public static final int MIN_PASSWORD_LENGTH = 6;

    private AccountRules() {
        //static helper, never instantiated
    }

    //except: the account being edited (it may keep its own name); null when creating
    public static String validateUsername(UserAccountDirectory uad, String un, UserAccount except) {

        if (un == null || un.isEmpty()) {
            return "Please enter a user name.";
        }
        if (!un.matches("[A-Za-z0-9._]{3,20}")) {
            return "User names must be 3-20 characters: letters, digits, '.' or '_'.";
        }
        UserAccount existing = uad.findUserAccountByUsername(un);
        if (existing != null && existing != except) {
            return "The user name \"" + un + "\" is already taken.";
        }
        return null;
    }

    public static String validatePassword(String pw, String confirm) {

        if (pw == null || pw.length() < MIN_PASSWORD_LENGTH) {
            return "Passwords must be at least " + MIN_PASSWORD_LENGTH + " characters.";
        }
        if (!pw.equals(confirm)) {
            return "The passwords do not match.";
        }
        return null;
    }

}
