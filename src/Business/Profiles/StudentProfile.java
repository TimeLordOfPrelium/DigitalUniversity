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
public class StudentProfile extends Profile {

    //the academic side of the student (transcript, course loads) lives in the university model
    info5100.university.example.Persona.StudentProfile academicprofile;

    public StudentProfile(Person p, info5100.university.example.Persona.StudentProfile ap) {
        super(p);
        academicprofile = ap;
    }

    @Override
    public String getRole() {
        return "Student";
    }

    public info5100.university.example.Persona.StudentProfile getAcademicProfile() {
        return academicprofile;
    }

}
