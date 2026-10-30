# Digital University System with Role-Based Access Control

INFO 5100 Midterm Project

## Team Information

| Team member | NUID | Role (use case) | Responsibilities |
|---|---|---|---|
| Philip Dixon | 003945527 | University Admin | Shared project setup and access-control layer; administer user accounts; register persons; manage student and faculty records; set the current semester; admin profile |
| Samuel Haddad | 003948956 | Faculty Member | Manage courses; grade students; performance reports; student profiles; faculty profile |
| James Russell | _NUID_ | Student | _responsibilities_ |

## Project Overview

This project integrates the course's **Digital University** reference model (courses,
schedules, seats, transcripts, faculty assignments) with an **access-control layer**, so
that users log in and each role can reach only the features it is authorized for. It is
built on the **ProfileWorkareaExample** skeleton (Swing work areas with CardLayout) and the
**Info5100 University Example** model.

**Objectives**

- Combine the provided model and access-control packages into one working application.
- Implement authentication (login and logout) and role-based authorization.
- Implement the Administrator, Faculty, and Student use cases with their responsibilities.

**Key features**

- Secure login and logout, with disabled accounts and per-feature permission checks
- **Admin:** user accounts, person registration, student and faculty records, current semester, own profile
- **Faculty:** course management, grading, performance reports, student profiles, own profile
- **Student:** _to be added_
- Input validation and confirmation dialogs throughout

## Installation & Setup Instructions

### Prerequisites

- JDK 17 or 21 (the project compiles to Java 8 bytecode, so any JDK 8 or later works)
- Apache NetBeans (any recent version)
- Git
- No external libraries are required.

### Setup

1. Clone the repository:

   ```
   git clone https://github.com/TimeLordOfPrelium/DigitalUniversity.git
   ```

2. In NetBeans, choose **File → Open Project** and select the cloned folder.
3. Right-click the project and choose **Clean and Build** (Shift+F11).
4. Choose **Run** (F6). The main class is `Business.ProfileWorkAreaMainFrame`.
5. Log in with one of the test accounts under [Testing Guide](#testing-guide).

All data is created in memory at startup by `src/Business/ConfigureABusiness.java`, so
every run starts from the same test data.

## Authentication & Access Control

Login and permission checks are handled by the classes described below rather than by
individual screens.

### Authentication

- **Login** (`ProfileWorkAreaMainFrame`): the user name and password are checked by
  `UserAccountDirectory.AuthenticateUser`. A wrong user name and a wrong password
  produce the same generic error message, which prevents user enumeration. Disabled
  accounts are refused even with the correct password, and the "account disabled"
  message is shown only after a correct password, so it doesn't reveal account
  information. A successful login records the login time and opens the work area for
  the account's role.
- **Logout** returns to the login screen and clears the logged-in user.

### Authorization

- `Business.AccessControl.AccessControl` holds the table of which role may use which
  `Feature`.
- Every work-area button calls `AccessGuard.allow(...)` before opening its screen, and an
  account that is disabled while logged in loses access on its next click.
- Faculty members see only their own classes and the students enrolled in them
  (`Business.Faculty.TeachingRoster`).
- Only the Admin role can change the current semester; Faculty and Student screens only
  read it.

### Access-control matrix

| Feature | Admin | Faculty | Student |
|---|:---:|:---:|:---:|
| Administer User Accounts | ✓ | | |
| Register Persons | ✓ | | |
| Manage Student Records | ✓ | | |
| Manage Faculty Records | ✓ | | |
| Set Current Semester | ✓ | | |
| Manage Courses | | ✓ | |
| Performance Reports | | ✓ | |
| View Student Profiles | | ✓ | |
| Course Work | | | ✓ |
| Course Registration | | | ✓ |
| Graduation Audit | | | ✓ |
| Transcript | | | ✓ |
| My Profile | ✓ | ✓ | ✓ |

To add a feature: add it to `Feature`, grant it to a role in `AccessControl`, and check
it with `AccessGuard.allow(...)` in the button that opens it.

## Features Implemented

### Administrator (Philip Dixon)

| Feature | Screen | Description |
|---|---|---|
| Administer User Accounts | Administer User Accounts → Create Account / Edit | List every login with its role, owner, status, and last login. Create a login for a registered person who has none, rename an account, reset its password, enable or disable it, or delete it. User names are 3–20 characters (letters, digits, `.` or `_`) and must be unique; passwords need at least 6 characters, typed twice. |
| Register Persons | Register Persons (HR) → Register New Person | List everyone registered and their roles. Register a new person as a Student, Faculty member, or Staff (Admin) with a validated name, unique email, and optional phone. |
| Manage Student Records | Manage Students → Student Record | Search students by ID, name, or email. View a student's details, login, credit hours, GPA, and course history with letter grades; update their name, email, and phone; or delete the record. |
| Manage Faculty Records | Manage Faculty → Faculty Record | Search faculty by ID, name, or email. View a faculty member's details, login, and teaching assignments with enrollment counts; update their details; or delete the record. |
| Set Current Semester | Set Current Semester | Choose the university's current semester from the semesters that have a course schedule. |
| Manage Own Profile | My Profile | Update the admin's own name, email, and phone, and change their password (the current password is required). |

Safeguards: an admin cannot disable or delete the account they are logged in with, and
the last active Admin account cannot be disabled or deleted. Deleting a student frees
every seat they hold; deleting a faculty member keeps their course offers on the schedule
with no teacher assigned.

### Faculty (Samuel Haddad)

| Feature | Screen | Description |
|---|---|---|
| Manage Courses | Manage Courses → Course Details | View classes by semester and update the room, meeting time, capacity, and description for current-semester courses. Capacity cannot be lower than the number of students enrolled. |
| Grade Students | Manage Courses → Grade Students | Assign or clear grades for students in current-semester classes. The grading system supports letter grades, including plus and minus grades where defined. |
| Manage Own Profile | My Profile | Update personal information such as name, email, phone number, title, office, office hours, and research area. |
| Performance Reports | Performance Reports | View class enrollment, number of graded students, average grade, highest grade, and lowest grade. Selecting a class also shows individual student grades and the grade distribution. |
| Student Profiles | Student Profiles → Student Profile | Search students enrolled in my classes and view their profiles, including hobbies, interests, GPA, completed and in-progress credits, degree progress, and course history. |

Only current-semester classes can be edited; past-semester classes are read-only.

### Student (James Russell)

_To be added._

## Usage Instructions

1. Run the project. The login screen appears.
2. Enter a user name and password from the test accounts in the
   [Testing Guide](#testing-guide) and click **Login**.
3. Your role's work area opens with one button per feature you are allowed to use.
4. Each screen has a **<< Back** button that returns to the previous screen.
5. Click **Logout** to return to the login screen.

### Example scenario: Admin onboards a new student

1. Log in as `admin` / `admin123`.
2. **Register Persons (HR) → Register New Person:** enter a name and a new email, choose
   **Student**, and click **Register**.
3. **Administer User Accounts → Create Account:** choose the new student, enter a user
   name and a password (twice), and save.
4. Log out and log in as the new student to confirm the Student work area opens.
5. Log back in as `admin` and use **Manage Students** to find and view the new record.

### Example scenario: Admin changes the current semester

1. Log in as `admin` / `admin123` → **Set Current Semester**.
2. Select Fall2025 and click **Make Current**. Faculty and Student screens now treat
   Fall2025 as the current semester.
3. Switch back to Fall2026 the same way.

### Example scenario: Faculty grades a student

1. Log in as `jdellon` / `faculty123` → **Manage Courses**.
2. Select INFO 6210 for Fall 2026 → **Grade Students**, select Adam Rollen, pick B+, and
   click **Assign Grade**.
3. Open **Performance Reports** and select INFO 6210 to see the updated class statistics.

### Example scenario: Student

_To be added._

## Testing Guide

### Test accounts

These are demo accounts for testing only.

| Role | Username | Password | Person |
|---|---|---|---|
| Admin | `admin` | `admin123` | John Smith |
| Admin | `gina` | `admin123` | Gina Montana |
| Faculty | `jdellon` | `faculty123` | Jim Dellon |
| Faculty | `ashnider` | `faculty123` | Anna Shnider |
| Student | `adam` | `student123` | Adam Rollen |
| Student | `laura` | `student123` | Laura Brown |
| Student | `jack` | `student123` | Jack While |

Maria Lopez is registered as a student but has no login yet, for testing account
creation.

**Seed data:** courses INFO 5100, INFO 6150, INFO 6205, INFO 6210, and INFO 6105. The
MSIS degree's core courses are INFO 5100 and INFO 6205; the others are electives.
Fall2025 is completed and graded; Fall2026 is the current semester, not graded yet.

### Verifying authentication

| Test | Expected result |
|---|---|
| Log in with a wrong password for `admin` | "Invalid user name or password." |
| Log in with a user name that does not exist | The same message (no hint whether the account exists) |
| Leave the user name or password empty | Asked to enter both |
| As admin, disable `laura`; then log in as `laura` / `student123` | "This account is disabled. Please contact a University Admin." |
| Log in, then click **Logout** | Back to the login screen; the work area is gone |

### Verifying authorization

| Test | Expected result |
|---|---|
| Log in as `jdellon` (Faculty) | Only Faculty features are offered; no Admin or Student screens |
| Log in as `adam` (Student) | Only Student features are offered |
| Log in as `admin` (Admin) | Only Admin features are offered |
| As `jdellon`, open Student Profiles | Only students enrolled in Jim Dellon's classes appear |

### Administrator test cases

1. **Administer User Accounts:** select your own account (`admin`) and try to delete or
   disable it; both should be refused. Click **Create Account**, choose Maria Lopez, and
   try the user name `ad` and a 3-character password; both should be rejected. Then
   create `mlopez` with password `student123`, log out, and confirm Maria can log in.
2. **Register Persons:** register a new person with the email `smith.j@northeastern.edu`;
   it should be rejected as already registered. Register them with a new email as a
   Student and confirm they appear in the list and under Manage Students.
3. **Manage Students:** search `brown` and confirm Laura Brown is the only result. Open
   Adam Rollen and check his course history (Fall2025 A and B+, two Fall2026 classes in
   progress). Change his phone and save. Then select Jack While, click **Delete**,
   confirm, and check that `jack` / `student123` can no longer log in.
4. **Manage Faculty:** open Jim Dellon and check his three teaching assignments. Delete
   Anna Shnider and confirm that `ashnider` / `faculty123` can no longer log in and that
   her course offers remain on the schedule with no teacher.
5. **Set Current Semester:** select Fall2025 and click **Make Current**, then log in as
   `jdellon` and check that Fall2025 classes are now the editable ones. Switch back to
   Fall2026.
6. **My Profile:** change the password from `admin123` to `admin456`, log out, and check
   that only the new password works. (Data resets when the program restarts.)

### Faculty test cases

1. **My Profile:** log in as `jdellon` / `faculty123`, open My Profile, and try entering
   `abc` as the email address. The system should show a validation warning. Enter a valid
   email and save again to confirm the update works. You can also try an email address
   already used by another account to make sure duplicates are rejected.
2. **Manage Courses:** open Manage Courses and select INFO 5100 for Fall 2026. Go to
   View / Edit Details and try setting the capacity to `abc` or `0`. Both should be
   rejected. Then set the capacity to `25` and change the room to check that the updates
   are saved. Switch to Fall 2025 and confirm that editing is disabled.
3. **Grade Students:** select INFO 6210 for Fall 2026 and open Grade Students. Select Adam
   Rollen, pick B+, and click Assign Grade. Then open a Fall 2025 class and check that its
   grades cannot be changed.
4. **Performance Reports:** open Performance Reports and check INFO 6210 for Fall 2026
   after assigning the B+ grade. Switch to Fall 2025 and select INFO 5100 to view the
   existing A and A- grades and the grade distribution.
5. **Student Profiles:** open Student Profiles and check that Adam, Jack, and Laura appear
   because they are enrolled in Jim Dellon's classes. Search for `lau` and confirm that
   Laura is the only result. Open Adam's profile and check his hobbies, interests, GPA of
   3.53 (A-), credits, and course history, including the newly assigned grade.

### Student test cases

_To be added._

## Challenges & Solutions

### Administrator (Philip Dixon)

- **Keeping two models in sync:** every student and faculty member exists twice, once in
  the access-control layer and once in the university model. I made `Business` the only
  place people are registered or deleted (`registerStudent`, `registerFaculty`,
  `deleteStudent`, `deleteFaculty`), so a login, a seat, or a teaching assignment is never
  left pointing at a record that no longer exists.
- **Deleting records safely:** deleting a student has to free every seat they hold, and
  deleting a faculty member has to unassign them from their courses without removing the
  courses. A person who still holds another role keeps their person record.
- **Gaps in the provided model:** the department never created its faculty directory, so
  registering a faculty member crashed. I fixed this in the model and added the lookups
  and removal methods the Admin screens needed.
- **Not locking the admins out:** I added checks so the last active Admin account and the
  account currently logged in cannot be disabled or deleted.

### Faculty (Samuel Haddad)

- **Finding the students in each class:** the seat assignment in the university model does
  not directly link back to a student. I had to work around this by adding
  `TeachingRoster`, which matches the course load associated with each seat against
  student transcripts to find the correct students.
- **Handling grades that have not been assigned:** the existing model uses 0 to represent a
  course that is still in progress. Because of this, I used 0 when a grade is cleared
  instead of treating it as an F. The grading screen lets faculty assign the letter grades
  supported by the application.
- **Changing class capacity:** reducing capacity gets tricky when students are already
  enrolled. I made it so the system removes empty seats when capacity is lowered and
  rejects the change if the new capacity is less than the current enrollment.

### Student (James Russell)

_To be added._

## Future Enhancements

**Administrator**

- Store passwords as salted hashes instead of plain text.
- Save data between runs instead of re-seeding at startup.
- Let the admin create new semesters and course schedules, not just choose the current one.
- Keep an audit log of account and record changes.

**Faculty**

- A way for faculty to post announcements and assignments for their classes.
- An option to export performance reports as CSV or PDF files.
- A view showing how student grades change across semesters.

**Student**

- _To be added._

## Contribution Breakdown

### Philip Dixon (Administrator)

- **Coding:** set up the shared project base (imported the skeleton and the university
  model, linked role profiles to the model, seeded the test data) and built the
  access-control layer (`Feature`, `AccessControl`, `AccessGuard`, secure login and
  logout). Built the Admin screens: Manage User Accounts, Create User Account, Administer
  User Account, Register Persons, Register Person, Manage Students, Student Record,
  Manage Faculty, Faculty Record, Set Current Semester, and My Profile, along with
  `PersonRules` and `AccountRules` for validation.
- **Testing:** tested the Admin screens with valid and invalid input (short or duplicate
  user names, weak or mismatched passwords, duplicate emails, missing names), the
  safeguards on the logged-in and last Admin accounts, that deleted users can no longer
  log in, and that Faculty and Student accounts cannot reach the Admin work area.
- **GitHub:** 15+ commits across the `setup/project-base`, `feature/admin-user-accounts`,
  `feature/admin-register-persons`, `feature/admin-records`, and
  `feature/admin-profile-docs` branches; reviewed and approved pull requests from other
  team members; set up the protected `main` branch.
- **Documentation:** wrote the README structure, the Authentication & Access Control
  section, and the Administrator sections. _Add slides / demo video._

### Samuel Haddad (Faculty)

- **Coding:** built the seven Faculty screens: My Profile, Manage Courses, Course Details,
  Grade Students, Performance Reports, Student Profiles, and the Student Profile detail
  page. Worked on the supporting models and logic, including `FacultyProfile`,
  `TeachingRoster`, `StudentProgress`, `GradeScale`, and `CourseOfferSummary`. Updated
  `CourseOffer` to support room, meeting time, description, and capacity, added hobbies
  and interests to `StudentProfile`, and added Faculty seed data in `ConfigureABusiness`.
- **Testing:** tested the Faculty screens with valid and invalid inputs, including invalid
  emails, duplicate emails, and invalid course capacities. Also checked that
  past-semester classes are read-only and that students and admins cannot access the
  Faculty work area.
- **GitHub:** 12 commits across five pull requests: `feature/faculty-profile`,
  `feature/faculty-manage-courses`, `feature/faculty-grading`,
  `feature/faculty-performance-reports`, and `feature/faculty-student-profiles`; reviewed
  and approved pull requests from other team members.
- **Documentation and demo:** wrote the Faculty sections of the README, prepared the
  Faculty slides, and worked on the Faculty part of the demo video.

### James Russell (Student)

_To be added._

## Demo and Presentation

- Demo video (YouTube, unlisted): _link_
- Presentation slides: _link or file name_

## Developer Notes

### Project structure

| Package | Contents |
|---|---|
| `Business` | `Business` (root object), `ConfigureABusiness` (seed data), `ProfileWorkAreaMainFrame` (login window) |
| `Business.AccessControl` | `Feature` (every protected feature) and `AccessControl` (role permissions) |
| `Business.Person`, `Business.Profiles`, `Business.UserAccounts` | Access-control layer: persons, role profiles, user accounts, validation rules |
| `info5100.university.example.*` | Digital University model: catalog, schedules, seats, transcripts, faculty |
| `UserInterface.WorkAreas.*` | One work area per role (Admin, Faculty, Student) |

Student and Faculty profiles each link to their academic record in the university model.
Always create and remove them through `Business` (`registerStudent`, `registerFaculty`,
`deleteStudent`, `deleteFaculty`) so both sides stay in sync.

### Team workflow

- `main` is protected: every change goes through a pull request with at least one
  teammate's approval.
- Work on your own branch (`feature/...`), commit in small steps, and build before every
  commit.
- Merge with **Create a merge commit** (not squash), and keep your branch after merging.

### Known limitations

- All data is held in memory and re-seeded at startup, so changes (including changed
  passwords) last only until the program is closed.
- Passwords are stored as plain text; a production system would store salted hashes.
