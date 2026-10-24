/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Business;

import Business.Person.Person;
import Business.Person.PersonDirectory;
import Business.Profiles.EmployeeDirectory;
import Business.Profiles.EmployeeProfile;
import Business.Profiles.FacultyDirectory;
import Business.Profiles.FacultyProfile;
import Business.Profiles.Profile;
import Business.Profiles.StudentDirectory;
import Business.Profiles.StudentProfile;

import Business.UserAccounts.UserAccountDirectory;

import info5100.university.example.Department.Department;

import java.util.ArrayList;

/**
 *
 * @author kal bugrara
 */
public class Business {

    String name;
    PersonDirectory persondirectory; //all people profiles regardless of the role

    EmployeeDirectory employeedirectory;
    UserAccountDirectory useraccountdirectory;
    StudentDirectory studentdirectory;
    FacultyDirectory facultydirectory;

    Department department; //the Digital University model: courses, schedules, transcripts



    public Business(String n) {
        name = n;

        persondirectory = new PersonDirectory();
        employeedirectory = new EmployeeDirectory(this);
        useraccountdirectory = new UserAccountDirectory();
        studentdirectory = new StudentDirectory();
        facultydirectory = new FacultyDirectory();
        department = new Department(n);


    }

    public String getName() {
        return name;
    }

    public PersonDirectory getPersonDirectory() {
        return persondirectory;
    }

    public UserAccountDirectory getUserAccountDirectory() {
        return useraccountdirectory;
    }


    public EmployeeDirectory getEmployeeDirectory() {
        return employeedirectory;
    }

    public StudentDirectory getStudentDirectory(){
        return studentdirectory;
    }

    public FacultyDirectory getFacultyDirectory(){
        return facultydirectory;
    }

    public Department getDepartment(){
        return department;
    }

    // The register methods below are the single place where a person gets a role.
    // They create the access-control profile and its matching record in the
    // university model together, using the same person id, so the two never drift apart.

    public StudentProfile registerStudent(Person p) {

        info5100.university.example.Persona.Person up = department.getPersonDirectory().newPerson(p.getPersonId());
        info5100.university.example.Persona.StudentProfile ap = department.getStudentDirectory().newStudentProfile(up);
        return studentdirectory.newStudentProfile(p, ap);
    }

    public FacultyProfile registerFaculty(Person p) {

        info5100.university.example.Persona.Person up = department.getPersonDirectory().newPerson(p.getPersonId());
        info5100.university.example.Persona.Faculty.FacultyProfile ap = department.getFacultyDirectory().newFacultyProfile(up);
        return facultydirectory.newFacultyProfile(p, ap);
    }

    public EmployeeProfile registerEmployee(Person p) {

        return employeedirectory.newEmployeeProfile(p);
    }

    //every role profile the person holds (a person can be e.g. both staff and student)
    public ArrayList<Profile> getProfilesFor(Person p) {

        ArrayList<Profile> profiles = new ArrayList<>();
        Profile found = studentdirectory.findStudent(p.getPersonId());
        if (found != null) {
            profiles.add(found);
        }
        found = facultydirectory.findFaculty(p.getPersonId());
        if (found != null) {
            profiles.add(found);
        }
        found = employeedirectory.findEmployee(p.getPersonId());
        if (found != null) {
            profiles.add(found);
        }
        return profiles;
    }

}
