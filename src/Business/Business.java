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

import Business.UserAccounts.UserAccount;
import Business.UserAccounts.UserAccountDirectory;

import info5100.university.example.CourseSchedule.CourseOffer;
import info5100.university.example.CourseSchedule.CourseSchedule;
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
    String currentsemester; //university-wide; only the Admin role may change it



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

    //the semester registration, transcripts and reports treat as "now"
    public String getCurrentSemester(){
        return currentsemester;
    }

    //only semesters that have a course schedule can become current
    public void setCurrentSemester(String semester){
        if (department.getCourseSchedule(semester) == null) {
            throw new IllegalArgumentException("No course schedule exists for " + semester);
        }
        currentsemester = semester;
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

    // The delete methods below undo a register method: they remove the login, the
    // university-model record and the role profile together, and drop the person
    // only when they hold no other role.

    //drops every class the student holds (freeing the seats) before removing the record.
    //Returns true if the person record was removed too (false when they still hold another role).
    public boolean deleteStudent(StudentProfile sp) {

        removeAccountFor(sp);
        info5100.university.example.Persona.StudentProfile ap = sp.getAcademicProfile();
        department.getStudentDirectory().removeStudent(ap);
        department.getPersonDirectory().removePerson(ap.getPerson());
        studentdirectory.removeStudent(sp);
        return removePersonIfNoRoles(sp.getPerson());
    }

    //unassigns the faculty member from every course they teach before removing the record.
    //Returns true if the person record was removed too (false when they still hold another role).
    public boolean deleteFaculty(FacultyProfile fp) {

        removeAccountFor(fp);
        info5100.university.example.Persona.Faculty.FacultyProfile ap = fp.getAcademicProfile();
        department.getFacultyDirectory().removeFaculty(ap);
        department.getPersonDirectory().removePerson(ap.getPerson());
        facultydirectory.removeFaculty(fp);
        return removePersonIfNoRoles(fp.getPerson());
    }

    private void removeAccountFor(Profile p) {

        UserAccount ua = useraccountdirectory.findUserAccountForProfile(p);
        if (ua != null) {
            useraccountdirectory.removeUserAccount(ua);
        }
    }

    private boolean removePersonIfNoRoles(Person p) {

        if (getProfilesFor(p).isEmpty()) {
            persondirectory.removePerson(p);
            return true;
        }
        return false;
    }

    //the semester whose schedule holds this course offer ("" if it is on none)
    public String getSemesterOf(CourseOffer co) {

        for (CourseSchedule cs : department.getCourseSchedules()) {
            if (cs.getSchedule().contains(co)) {
                return cs.getSemester();
            }
        }
        return "";
    }

    //registered role profiles that do not have a login yet (candidates for a new account)
    public ArrayList<Profile> getProfilesWithoutAccount() {

        ArrayList<Profile> all = new ArrayList<>();
        all.addAll(employeedirectory.getEmployeeList());
        all.addAll(facultydirectory.getFacultyList());
        all.addAll(studentdirectory.getStudentList());

        ArrayList<Profile> result = new ArrayList<>();
        for (Profile p : all) {
            if (useraccountdirectory.findUserAccountForProfile(p) == null) {
                result.add(p);
            }
        }
        return result;
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
