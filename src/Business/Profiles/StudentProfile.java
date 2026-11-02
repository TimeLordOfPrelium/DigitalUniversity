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
 * @author Philip Dixon (modified)
 * @author Samuel Haddad (modified)
 */
public class StudentProfile extends Profile {

    //the academic side of the student (transcript, course loads) lives in the university model
    info5100.university.example.Persona.StudentProfile academicprofile;

    // Faculty use case (Samuel Haddad): shown to faculty on the student's profile
    String hobbies;
    String interests;

    public StudentProfile(Person p, info5100.university.example.Persona.StudentProfile ap) {
        super(p);
        academicprofile = ap;
        hobbies = "";
        interests = "";
    }

    @Override
    public String getRole() {
        return AccessControl.ROLE_STUDENT;
    }

    public info5100.university.example.Persona.StudentProfile getAcademicProfile() {
        return academicprofile;
    }

    // Student use case: coursework submissions as {course #, assignment, submitted at}
    java.util.ArrayList<String[]> submissions = new java.util.ArrayList<>();

    public void addSubmission(String coursenumber, String assignment, String when) {
        submissions.add(new String[]{coursenumber, assignment, when});
    }

    public java.util.ArrayList<String[]> getSubmissions() {
        return submissions;
    }

    public int getSubmissionCount(String coursenumber) {
        int n = 0;
        for (String[] s : submissions) {
            if (s[0].equals(coursenumber)) {
                n++;
            }
        }
        return n;
    }

    public String getHobbies() {
        return hobbies;
    }

    public void setHobbies(String hobbies) {
        this.hobbies = hobbies;
    }

    public String getInterests() {
        return interests;
    }

    public void setInterests(String interests) {
        this.interests = interests;
    }

}
