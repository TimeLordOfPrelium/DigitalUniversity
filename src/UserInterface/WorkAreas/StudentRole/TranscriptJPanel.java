package UserInterface.WorkAreas.StudentRole;

import Business.Profiles.StudentProfile;
import info5100.university.example.CourseSchedule.CourseLoad;
import info5100.university.example.CourseSchedule.GradeScale;
import info5100.university.example.CourseSchedule.SeatAssignment;
import info5100.university.example.Persona.Transcript;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 * Student use case: Review Transcript.
 * Shows every course the student has taken, by semester, with credits and grade,
 * plus total credits and GPA.
 *
 * @author James Russell
 */
public class TranscriptJPanel extends JPanel {

    JPanel CardSequencePanel;
    StudentProfile student;

    public TranscriptJPanel(StudentProfile sp, JPanel clp) {
        student = sp;
        CardSequencePanel = clp;
        setLayout(new BorderLayout(10, 10));

        JLabel title = new JLabel("My Transcript - " + student.getPerson().getFullName());
        title.setFont(new java.awt.Font("Dialog", java.awt.Font.BOLD, 20));
        add(title, BorderLayout.NORTH);

        DefaultTableModel model = new DefaultTableModel(
                new Object[]{"Semester", "Course #", "Course Name", "Credits", "Grade"}, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false; //read-only view
            }
        };

        Transcript t = student.getAcademicProfile() == null ? null : student.getAcademicProfile().getTranscript();
        if (t != null) {
            for (CourseLoad cl : t.getCourseLoads()) {
                for (SeatAssignment sa : cl.getSeatAssignments()) {
                    float g = sa.getGrade();
                    model.addRow(new Object[]{
                        cl.getSemester(),
                        sa.getAssociatedCourse().getCOurseNumber(),
                        sa.getAssociatedCourse().getName(),
                        sa.getCreditHours(),
                        GradeScale.isGraded(g) ? GradeScale.toLetter(g) : GradeScale.NOT_GRADED});
                }
            }
        }
        add(new JScrollPane(new JTable(model)), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton back = new JButton("<< Back");
        back.addActionListener(e -> {
            CardSequencePanel.remove(this);
            ((CardLayout) CardSequencePanel.getLayout()).last(CardSequencePanel);
        });
        bottom.add(back);
        if (t != null) {
            bottom.add(new JLabel("   Total credits: " + t.getTotalCreditHours()
                    + "    GPA: " + String.format("%.2f", t.getGPA())));
        } else {
            bottom.add(new JLabel("   No academic record found."));
        }
        add(bottom, BorderLayout.SOUTH);
    }
}
