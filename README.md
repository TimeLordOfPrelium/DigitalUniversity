# Digital University System

INFO 5100 midterm project. A Digital University (courses, schedules, transcripts,
faculty assignments) behind an access-control layer: users log in, and each role
only reaches the features it is authorized for.

Built on the course's **ProfileWorkareaExample** skeleton (access control + Swing
work areas) and the **Info5100 University Example** model.

## Use cases

| Use case | Role | Owner |
|---|---|---|
| Administrator | University Admin | _name_ |
| Faculty | Faculty Member | _name_ |
| Student | Student | _name_ |

## Running the project

1. Install a JDK (17 or 21 recommended) and Apache NetBeans.
2. Clone this repository and open the folder with **File → Open Project**.
3. **Clean and Build** (Shift+F11), then **Run** (F6).
   The main class is `Business.ProfileWorkAreaMainFrame`.

## Test logins

Seeded at startup by `src/Business/ConfigureABusiness.java`. These are demo
accounts for testing only.

| Role | Username | Password | Person |
|---|---|---|---|
| Admin | `admin` | `admin123` | John Smith |
| Admin | `gina` | `admin123` | Gina Montana |
| Faculty | `jdellon` | `faculty123` | Jim Dellon |
| Faculty | `ashnider` | `faculty123` | Anna Shnider |
| Student | `adam` | `student123` | Adam Rollen |
| Student | `laura` | `student123` | Laura Brown |
| Student | `jack` | `student123` | Jack While |

Maria Lopez is registered as a student but has no login yet, for testing
account creation.

## Seed data

- Courses: INFO 5100, INFO 6150, INFO 6205, INFO 6210, INFO 6105
- MSIS degree: core INFO 5100 and INFO 6205; the others are electives
- Fall2025: completed and graded. Fall2026: current semester, not graded yet

## Project structure

| Package | Contents |
|---|---|
| `Business` | `Business` (root object), `ConfigureABusiness` (seed data), `ProfileWorkAreaMainFrame` (login window) |
| `Business.Person`, `Business.Profiles`, `Business.UserAccounts` | Access-control layer: persons, role profiles, user accounts |
| `info5100.university.example.*` | Digital University model: catalog, schedules, seats, transcripts, faculty |
| `UserInterface.WorkAreas.*` | One work area per role (Admin, Faculty, Student) |

Student and Faculty profiles each link to their academic record in the
university model. Always create them through `Business.registerStudent` /
`registerFaculty` so both sides stay in sync.

## Team workflow

- `main` is protected: every change goes through a pull request with at least
  one teammate's approval.
- Work on your own branch (`feature/...`), commit in small steps, and build
  before every commit.
- Merge with **Create a merge commit** (not squash), and keep your branch after
  merging.
