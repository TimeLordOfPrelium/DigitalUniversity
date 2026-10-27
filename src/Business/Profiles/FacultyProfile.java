/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Business.Profiles;

import Business.AccessControl.AccessControl;
import Business.Person.Person;

/**
 *
 * @author kal bugrara
 */
public class FacultyProfile extends Profile {

    //the teaching side of the faculty member (course assignments, ratings) lives in the university model
    info5100.university.example.Persona.Faculty.FacultyProfile academicprofile;

    // Faculty use case (Samuel Haddad): details the faculty member keeps up to date on My Profile
    String title;        //e.g. Associate Professor
    String office;       //building and room
    String officehours;  //free text, e.g. Tue 2-4pm
    String researcharea;

    public FacultyProfile(Person p, info5100.university.example.Persona.Faculty.FacultyProfile ap) {
        super(p);
        academicprofile = ap;
        title = "";
        office = "";
        officehours = "";
        researcharea = "";
    }

    @Override
    public String getRole() {
        return AccessControl.ROLE_FACULTY;
    }

    public info5100.university.example.Persona.Faculty.FacultyProfile getAcademicProfile() {
        return academicprofile;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }

    public String getOfficeHours() {
        return officehours;
    }

    public void setOfficeHours(String officehours) {
        this.officehours = officehours;
    }

    public String getResearchArea() {
        return researcharea;
    }

    public void setResearchArea(String researcharea) {
        this.researcharea = researcharea;
    }

}
