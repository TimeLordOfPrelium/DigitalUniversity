/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Business.UserAccounts;

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
}
