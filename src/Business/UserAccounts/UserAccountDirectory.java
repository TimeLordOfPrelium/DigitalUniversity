/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Business.UserAccounts;

import Business.AccessControl.AccessControl;
import Business.Profiles.Profile;

import java.util.ArrayList;

/**
 *
 * @author kal bugrara
 */
public class UserAccountDirectory {

      ArrayList<UserAccount> useraccountlist ;

      public UserAccountDirectory (){

       useraccountlist = new ArrayList();

    }

    public UserAccount newUserAccount(Profile p, String un, String pw) {

        UserAccount ua = new UserAccount (p,  un,  pw);
        useraccountlist.add(ua);
        return ua;
    }

    public UserAccount findUserAccount(String id) {

        for (UserAccount ua : useraccountlist) {

            if (ua.isMatch(id)) {
                return ua;
            }
        }
            return null; //not found after going through the whole list
         }

    public UserAccount findUserAccountForProfile(Profile p) {

        for (UserAccount ua : useraccountlist) {

            if (ua.getAssociatedPersonProfile() == p) {
                return ua;
            }
        }
        return null;
    }

    //user names are case-insensitive, matching how IsValidUser compares them
    public UserAccount findUserAccountByUsername(String un) {

        for (UserAccount ua : useraccountlist) {

            if (ua.getUserLoginName().equalsIgnoreCase(un)) {
                return ua;
            }
        }
        return null;
    }

    //returns the account only if the credentials match and the account is active
     public UserAccount AuthenticateUser(String un, String pw) {

        for (UserAccount ua : useraccountlist) {

            if (ua.IsValidUser(un, pw) && ua.isActive()) {
                return ua;
            }
        }
            return null; //not found after going through the whole list
         }
     public ArrayList<UserAccount> getUserAccountList()
     {
         return useraccountlist;
     }

    //removes the login only; the person and their profile stay registered
    public void removeUserAccount(UserAccount ua) {
        useraccountlist.remove(ua);
    }

    public int countActiveAccounts(String role) {

        int count = 0;
        for (UserAccount ua : useraccountlist) {
            if (ua.isActive() && ua.getRole().equals(role)) {
                count++;
            }
        }
        return count;
    }

    //the university must always keep at least one admin who can log in
    public boolean isLastActiveAdmin(UserAccount ua) {

        return ua.isActive()
                && ua.getRole().equals(AccessControl.ROLE_ADMIN)
                && countActiveAccounts(AccessControl.ROLE_ADMIN) <= 1;
    }
}
