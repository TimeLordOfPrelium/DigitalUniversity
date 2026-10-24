/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Business.Person;

import java.util.ArrayList;

/**
 *
 * @author kal bugrara
 */
public class PersonDirectory {

      ArrayList<Person> personlist ;
      int nextid = 1001; //university ids are issued in sequence

      public PersonDirectory (){

       personlist = new ArrayList();

    }

    public Person newPerson(String firstName, String lastName, String email) {

        Person p = new Person("NU" + nextid, firstName, lastName, email);
        nextid++;
        personlist.add(p);
        return p;
    }

    public Person findPerson(String id) {

        for (Person p : personlist) {

            if (p.isMatch(id)) {
                return p;
            }
        }
            return null; //not found after going through the whole list
         }

    public Person findPersonByEmail(String email) {

        for (Person p : personlist) {

            if (p.getEmail().equalsIgnoreCase(email)) {
                return p;
            }
        }
        return null;
    }

    public ArrayList<Person> getPersonList() {
        return personlist;
    }

    public void removePerson(Person p) {
        personlist.remove(p);
    }

}
