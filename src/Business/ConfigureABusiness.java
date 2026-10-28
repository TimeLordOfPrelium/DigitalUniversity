/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.

*/
package Business;

import Business.Person.Person;
import Business.Person.PersonDirectory;
import Business.Profiles.EmployeeProfile;
import Business.Profiles.FacultyProfile;
import Business.Profiles.StudentProfile;

import Business.UserAccounts.UserAccountDirectory;

import info5100.university.example.CourseSchedule.CourseLoad;
import info5100.university.example.CourseSchedule.CourseOffer;
import info5100.university.example.CourseSchedule.CourseSchedule;
import info5100.university.example.CourseSchedule.SeatAssignment;
import info5100.university.example.Department.Department;


/**
 *
 * @author kal bugrara
 */
class ConfigureABusiness {

    static Business initialize() {
        Business business = new Business("Information Systems");

// Create Persons
        PersonDirectory persondirectory = business.getPersonDirectory();
        Person person001 = persondirectory.newPerson("John", "Smith", "smith.j@northeastern.edu");
        Person person002 = persondirectory.newPerson("Gina", "Montana", "montana.g@northeastern.edu");
        Person person003 = persondirectory.newPerson("Adam", "Rollen", "rollen.a@northeastern.edu");
        Person person005 = persondirectory.newPerson("Jim", "Dellon", "dellon.j@northeastern.edu");
        Person person006 = persondirectory.newPerson("Anna", "Shnider", "shnider.a@northeastern.edu");
        Person person007 = persondirectory.newPerson("Laura", "Brown", "brown.l@northeastern.edu");
        Person person008 = persondirectory.newPerson("Jack", "While", "while.j@northeastern.edu");
        Person person009 = persondirectory.newPerson("Maria", "Lopez", "lopez.m@northeastern.edu"); //registered, no login yet

// Give each person a role (creates the matching record in the university model)
        EmployeeProfile admin1 = business.registerEmployee(person001);
        EmployeeProfile admin2 = business.registerEmployee(person002);

        FacultyProfile faculty1 = business.registerFaculty(person005);
        FacultyProfile faculty2 = business.registerFaculty(person006);

        StudentProfile student1 = business.registerStudent(person003);
        StudentProfile student2 = business.registerStudent(person007);
        StudentProfile student3 = business.registerStudent(person008);
        business.registerStudent(person009);

// Course catalog and degree requirements
        Department department = business.getDepartment();
        department.newCourse("Application Engineering and Development", "INFO 5100", 4);
        department.newCourse("Web Design and User Experience", "INFO 6150", 4);
        department.newCourse("Program Structures and Algorithms", "INFO 6205", 4);
        department.newCourse("Database Management and Design", "INFO 6210", 4);
        department.newCourse("Data Science Engineering Methods", "INFO 6105", 4);

        department.addCoreCourse(department.getCourseCatalog().getCourseByNumber("INFO 5100"));
        department.addCoreCourse(department.getCourseCatalog().getCourseByNumber("INFO 6205"));
        department.addElectiveCourse(department.getCourseCatalog().getCourseByNumber("INFO 6150"));
        department.addElectiveCourse(department.getCourseCatalog().getCourseByNumber("INFO 6210"));
        department.addElectiveCourse(department.getCourseCatalog().getCourseByNumber("INFO 6105"));

// Last year's schedule (completed, graded) and the current semester (in progress)
        CourseSchedule fall2025 = department.newCourseSchedule("Fall2025");
        newCourseOffer(fall2025, "INFO 5100", faculty1, 20);
        newCourseOffer(fall2025, "INFO 6150", faculty2, 20);

        CourseSchedule fall2026 = department.newCourseSchedule("Fall2026");
        newCourseOffer(fall2026, "INFO 5100", faculty1, 20);
        newCourseOffer(fall2026, "INFO 6205", faculty2, 20);
        newCourseOffer(fall2026, "INFO 6150", faculty2, 20);
        newCourseOffer(fall2026, "INFO 6210", faculty1, 15);

        business.setCurrentSemester("Fall2026");

// Enrollments. Older semesters first so the newest one becomes the current course load.
        enroll(student1, fall2025, "INFO 5100", 4.0f);
        enroll(student1, fall2025, "INFO 6150", 3.3f);
        enroll(student1, fall2026, "INFO 6205", 0f);
        enroll(student1, fall2026, "INFO 6210", 0f);

        enroll(student2, fall2025, "INFO 5100", 3.7f);
        enroll(student2, fall2026, "INFO 6205", 0f);
        enroll(student2, fall2026, "INFO 6150", 0f);

        enroll(student3, fall2026, "INFO 5100", 0f);

// Create User accounts that link to specific profiles
        UserAccountDirectory uadirectory = business.getUserAccountDirectory();
        uadirectory.newUserAccount(admin1, "admin", "admin123");
        uadirectory.newUserAccount(admin2, "gina", "admin123");
        uadirectory.newUserAccount(faculty1, "jdellon", "faculty123");
        uadirectory.newUserAccount(faculty2, "ashnider", "faculty123");
        uadirectory.newUserAccount(student1, "adam", "student123");
        uadirectory.newUserAccount(student2, "laura", "student123");
        uadirectory.newUserAccount(student3, "jack", "student123");

// Faculty use case seed data (Samuel Haddad)
        faculty1.setTitle("Associate Professor");
        faculty1.setOffice("Dodge Hall 210");
        faculty1.setOfficeHours("Tue 2:00-4:00 PM");
        faculty1.setResearchArea("Software Engineering");

        faculty2.setTitle("Assistant Teaching Professor");
        faculty2.setOffice("Hurtig Hall 115");
        faculty2.setOfficeHours("Thu 10:00 AM-12:00 PM");
        faculty2.setResearchArea("Data Science");

        setCourseDetails(fall2025, "INFO 5100", "Snell Library 033", "Mon/Wed 10:00-11:40 AM",
                "Object-oriented design and Java Swing application development.");
        setCourseDetails(fall2025, "INFO 6150", "Ryder Hall 155", "Tue 6:00-9:00 PM",
                "Front-end web design, accessibility and user experience.");
        setCourseDetails(fall2026, "INFO 5100", "Snell Library 033", "Mon/Wed 10:00-11:40 AM",
                "Object-oriented design and Java Swing application development.");
        setCourseDetails(fall2026, "INFO 6205", "Richards Hall 300", "Tue/Fri 1:35-3:15 PM",
                "Data structures, algorithm analysis and program structure.");
        setCourseDetails(fall2026, "INFO 6150", "Ryder Hall 155", "Thu 6:00-9:00 PM",
                "Front-end web design, accessibility and user experience.");
        setCourseDetails(fall2026, "INFO 6210", "Dodge Hall 050", "Wed 6:00-9:00 PM",
                "Relational design, SQL and database administration.");

        return business;

    }

    static CourseOffer newCourseOffer(CourseSchedule cs, String coursenumber, FacultyProfile teacher, int seats) {

        CourseOffer co = cs.newCourseOffer(coursenumber);
        co.generatSeats(seats);
        co.AssignAsTeacher(teacher.getAcademicProfile());
        return co;
    }

    //room, meeting time and description of a course offer (Faculty use case seed data)
    static void setCourseDetails(CourseSchedule cs, String coursenumber, String room, String time, String description) {

        CourseOffer co = cs.getCourseOfferByNumber(coursenumber);
        if (co == null) {
            return;
        }
        co.setRoom(room);
        co.setMeetingTime(time);
        co.setDescription(description);
    }

    //registers the student in a class; grade 0 means not graded yet
    static void enroll(StudentProfile sp, CourseSchedule cs, String coursenumber, float grade) {

        info5100.university.example.Persona.StudentProfile ap = sp.getAcademicProfile();
        CourseLoad cl = ap.getCourseLoadBySemester(cs.getSemester());
        if (cl == null) {
            cl = ap.newCourseLoad(cs.getSemester());
        }
        SeatAssignment sa = cs.getCourseOfferByNumber(coursenumber).assignEmptySeat(cl);
        if (sa != null) {
            sa.setGrade(grade);
        }
    }

}
