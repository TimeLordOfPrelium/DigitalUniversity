/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UserInterface;

import Business.AccessControl.AccessControl;
import Business.AccessControl.Feature;
import Business.UserAccounts.UserAccount;

import java.awt.Component;
import javax.swing.JOptionPane;

/**
 * UI side of authorization. Every work-area button calls allow(...) before it
 * opens a screen, so a feature is never reachable without permission.
 *
 * @author Philip Dixon
 */
public class AccessGuard {

    private AccessGuard() {
        //static helper, never instantiated
    }

    //checks the feature and tells the user when access is refused
    public static boolean allow(Component parent, UserAccount ua, Feature f) {

        if (AccessControl.isAuthorized(ua, f)) {
            return true;
        }
        JOptionPane.showMessageDialog(parent, "You are not authorized to use " + f.getDisplayName() + ".",
                "Access denied", JOptionPane.ERROR_MESSAGE);
        return false;
    }

    //placeholder for a feature whose screen has not been built yet
    public static void comingSoon(Component parent, Feature f) {

        JOptionPane.showMessageDialog(parent, f.getDisplayName() + " is not available yet.",
                "Coming soon", JOptionPane.INFORMATION_MESSAGE);
    }

}
