/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Business.AccessControl;

/**
 * Every protected feature in the system. A work area asks AccessControl
 * whether the logged-in user may use a feature before opening its screen.
 *
 * To add a feature: add it here, then grant it to a role in AccessControl.
 */
public enum Feature {

    // Administrator
    MANAGE_USER_ACCOUNTS("Administer User Accounts"),
    REGISTER_PERSONS("Register Persons"),
    MANAGE_STUDENT_RECORDS("Manage Student Records"),
    MANAGE_FACULTY_RECORDS("Manage Faculty Records"),
    SET_CURRENT_SEMESTER("Set Current Semester"),

    // Faculty
    MANAGE_COURSES("Manage Courses"),
    PERFORMANCE_REPORTS("Performance Reports"),
    VIEW_STUDENT_PROFILES("View Student Profiles"),

    // Student
    COURSEWORK("Course Work"),
    COURSE_REGISTRATION("Course Registration"),
    GRADUATION_AUDIT("Graduation Audit"),
    TRANSCRIPT("Transcript"),

    // Every role
    MANAGE_OWN_PROFILE("My Profile");

    private final String displayname;

    Feature(String n) {
        displayname = n;
    }

    public String getDisplayName() {
        return displayname;
    }

    @Override
    public String toString() {
        return displayname;
    }
}
