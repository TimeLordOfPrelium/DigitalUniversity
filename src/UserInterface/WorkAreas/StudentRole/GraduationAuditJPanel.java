package UserInterface.WorkAreas.StudentRole;

import Business.Business;
import Business.Profiles.StudentProfile;
import info5100.university.example.CourseCatalog.Course;
import info5100.university.example.CourseSchedule.SeatAssignment;
import info5100.university.example.Degree.Degree;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 * Student use case: Graduation Audit.
 * Compares the transcript to the degree: core courses, electives and total credits.
 *
 * @author James Russell
 */
public class GraduationAuditJPanel extends JPanel {

    public static final int REQUIRED_CREDITS = 32; //NEU MS requirement used by the model

    JPanel CardSequencePanel;

    public GraduationAuditJPanel(Business business, StudentProfile student, JPanel clp) {
        CardSequencePanel = clp;
        setLayout(new BorderLayout(10, 10));

        JLabel title = new JLabel("Graduation Audit - " + student.getPerson().getFullName());
        title.setFont(new java.awt.Font("Dialog", java.awt.Font.BOLD, 20));
        add(title, BorderLayout.NORTH);

        DefaultTableModel model = new DefaultTableModel(new Object[]{"Requirement", "Course #", "Course Name", "Status"}, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        Degree degree = business.getDepartment().getDegree();
        ArrayList<SeatAssignment> taken = new ArrayList<>();
        int credits = 0;
        if (student.getAcademicProfile() != null) {
            taken = student.getAcademicProfile().getCourseList();
            credits = student.getAcademicProfile().getTranscript().getTotalCreditHours();
        }

        boolean coreOk = true;
        int electivesTaken = 0;
        if (degree != null) {
            for (Course c : degree.getCoreCourses()) {
                boolean done = degree.isCoreSatisfied(taken, c);
                coreOk = coreOk && done;
                model.addRow(new Object[]{"Core", c.getCOurseNumber(), c.getName(), done ? "Completed" : "Missing"});
            }
            for (Course c : degree.getElectiveCourses()) {
                boolean done = degree.isCoreSatisfied(taken, c); //same "is it on the transcript" check
                model.addRow(new Object[]{"Elective", c.getCOurseNumber(), c.getName(), done ? "Completed" : "Not taken"});
            }
            electivesTaken = degree.getTotalElectiveCoursesTaken(taken);
        }
        add(new JScrollPane(new JTable(model)), BorderLayout.CENTER);

        boolean creditsOk = credits >= REQUIRED_CREDITS;
        boolean ready = degree != null && coreOk && creditsOk;

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton back = new JButton("<< Back");
        back.addActionListener(e -> {
            CardSequencePanel.remove(this);
            ((CardLayout) CardSequencePanel.getLayout()).last(CardSequencePanel);
        });
        bottom.add(back);
        bottom.add(new JLabel("   Credits: " + credits + " / " + REQUIRED_CREDITS
                + "    Electives taken: " + electivesTaken
                + "    Core: " + (coreOk ? "complete" : "incomplete")
                + "    Result: " + (ready ? "READY TO GRADUATE" : "NOT YET ELIGIBLE")));
        add(bottom, BorderLayout.SOUTH);
    }
}
