/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Business.UserAccounts;

import Business.Profiles.Profile;

import java.util.Date;



/**
 *
 * @author kal bugrara
 */
public class UserAccount {

    Profile profile;
    String username;
    String password;
    boolean active;     //disabled accounts cannot log in
    Date lastupdated;
    Date lastlogin;     //null until the first login

    public UserAccount (Profile profile, String un, String pw){
        username = un;
         password = pw;
         this.profile = profile;
         active = true;
         lastupdated = new Date();

    }

    public String getPersonId(){
        return profile.getPerson().getPersonId();
    }
    public String getUserLoginName(){
        return username;
    }

    public void setUserLoginName(String un){
        username = un;
        touch();
    }

    public void setPassword(String pw){
        password = pw;
        touch();
    }

    public boolean isPasswordCorrect(String pw){
        return password.equals(pw);
    }

        public boolean isMatch(String id){
        if(getPersonId().equals(id)) return true;
        return false;
    }
        public boolean IsValidUser(String un, String pw){

            if (username.equalsIgnoreCase(un) && password.equals(pw)) return true;
            else return false;

        }
        public String getRole(){
            return profile.getRole();
        }

        public Profile getAssociatedPersonProfile(){
            return profile;
        }

    public boolean isActive(){
        return active;
    }

    public void setActive(boolean a){
        active = a;
        touch();
    }

    public String getStatus(){
        return active ? "Active" : "Disabled";
    }

    public Date getLastUpdated(){
        return lastupdated;
    }

    public Date getLastLogin(){
        return lastlogin;
    }

    public void recordLogin(){
        lastlogin = new Date();
    }

    private void touch(){
        lastupdated = new Date();
    }

    @Override
        public String toString(){

            return getUserLoginName();
        }

}

