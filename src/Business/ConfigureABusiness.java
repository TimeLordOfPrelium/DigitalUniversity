/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.

*/
package Business;

import Business.Person.Person;
import Business.Person.PersonDirectory;
import Business.Profiles.EmployeeProfile;
import Business.Profiles.StudentProfile;

import Business.UserAccounts.UserAccount;
import Business.UserAccounts.UserAccountDirectory;


/**
 *
 * @author kal bugrara
 */
class ConfigureABusiness {

    static Business initialize() {
        Business business = new Business("Information Systems");

// Create Persons
      PersonDirectory persondirectory = business.getPersonDirectory();
        Person person001 = persondirectory.newPerson("John", "Smith", "john.smith@northeastern.edu");
        Person person003 = persondirectory.newPerson("Adam", "Rollen", "rollen.a@northeastern.edu");

// Create Admins to manage the business
        EmployeeProfile employeeprofile0 = business.registerEmployee(person001);

        StudentProfile studentprofile0 = business.registerStudent(person003);



// Create User accounts that link to specific profiles
        UserAccountDirectory uadirectory = business.getUserAccountDirectory();
        UserAccount ua3 = uadirectory.newUserAccount(employeeprofile0, "admin", "****");
        UserAccount ua4 = uadirectory.newUserAccount(studentprofile0, "adam", "****");

        return business;

    }

}
