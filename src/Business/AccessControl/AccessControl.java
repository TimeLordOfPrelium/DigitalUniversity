/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Business.AccessControl;

import Business.UserAccounts.UserAccount;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Set;

/**
 * Role-based authorization: which features each role may use.
 * This table is the single source of truth; the UI never decides on its own.
 */
public class AccessControl {

    public static final String ROLE_ADMIN = "Admin";
    public static final String ROLE_FACULTY = "Faculty";
    public static final String ROLE_STUDENT = "Student";

    private static final HashMap<String, EnumSet<Feature>> permissions = new HashMap<>();

    static {
        permissions.put(ROLE_ADMIN, EnumSet.of(
                Feature.MANAGE_USER_ACCOUNTS,
                Feature.REGISTER_PERSONS,
                Feature.MANAGE_STUDENT_RECORDS,
                Feature.MANAGE_FACULTY_RECORDS,
                Feature.SET_CURRENT_SEMESTER,
                Feature.MANAGE_OWN_PROFILE));

        permissions.put(ROLE_FACULTY, EnumSet.of(
                Feature.MANAGE_COURSES,
                Feature.PERFORMANCE_REPORTS,
                Feature.VIEW_STUDENT_PROFILES,
                Feature.MANAGE_OWN_PROFILE));

        permissions.put(ROLE_STUDENT, EnumSet.of(
                Feature.COURSEWORK,
                Feature.COURSE_REGISTRATION,
                Feature.GRADUATION_AUDIT,
                Feature.TRANSCRIPT,
                Feature.MANAGE_OWN_PROFILE));
    }

    private AccessControl() {
        //static helper, never instantiated
    }

    //true only for an active account whose role has been granted the feature
    public static boolean isAuthorized(UserAccount ua, Feature f) {

        if (ua == null || !ua.isActive()) {
            return false;
        }
        Set<Feature> allowed = permissions.get(ua.getRole());
        return allowed != null && allowed.contains(f);
    }

    public static Set<Feature> getFeatures(String role) {

        EnumSet<Feature> allowed = permissions.get(role);
        if (allowed == null) {
            return EnumSet.noneOf(Feature.class);
        }
        return EnumSet.copyOf(allowed);
    }

}
