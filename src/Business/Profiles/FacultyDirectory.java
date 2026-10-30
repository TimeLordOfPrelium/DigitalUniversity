/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Business.Profiles;

import Business.Person.Person;

import java.util.ArrayList;

/**
 *
 * @author Philip Dixon
 */
public class FacultyDirectory {

    ArrayList<FacultyProfile> facultylist;

    public FacultyDirectory() {

        facultylist = new ArrayList();

    }

    public FacultyProfile newFacultyProfile(Person p, info5100.university.example.Persona.Faculty.FacultyProfile ap) {

        FacultyProfile fp = new FacultyProfile(p, ap);
        facultylist.add(fp);
        return fp;
    }

    public FacultyProfile findFaculty(String id) {

        for (FacultyProfile fp : facultylist) {

            if (fp.isMatch(id)) {
                return fp;
            }
        }
        return null; //not found after going through the whole list
    }

    public ArrayList<FacultyProfile> getFacultyList() {
        return facultylist;
    }

    public void removeFaculty(FacultyProfile fp) {
        facultylist.remove(fp);
    }

}
