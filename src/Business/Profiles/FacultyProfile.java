/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Business.Profiles;

import Business.Person.Person;

/**
 *
 * @author kal bugrara
 */
public class FacultyProfile extends Profile {

    //the teaching side of the faculty member (course assignments, ratings) lives in the university model
    info5100.university.example.Persona.Faculty.FacultyProfile academicprofile;

    public FacultyProfile(Person p, info5100.university.example.Persona.Faculty.FacultyProfile ap) {
        super(p);
        academicprofile = ap;
    }

    @Override
    public String getRole() {
        return "Faculty";
    }

    public info5100.university.example.Persona.Faculty.FacultyProfile getAcademicProfile() {
        return academicprofile;
    }

}
