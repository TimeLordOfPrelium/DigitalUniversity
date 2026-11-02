package UserInterface.WorkAreas.StudentRole;

import Business.Business;
import Business.Profiles.StudentProfile;
import info5100.university.example.CourseSchedule.CourseLoad;
import info5100.university.example.CourseSchedule.GradeScale;
import info5100.university.example.CourseSchedule.SeatAssignment;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

/**
 * Student use case: Manage Coursework (submit assignments, track progress)
 * for the classes the student is taking in the current semester.
 *
 * @author James Russell
 */
public class CourseWorkJPanel extends JPanel {

    JPanel CardSequencePanel;
    Business business;
    StudentProfile student;
    DefaultTableModel courseModel;
    DefaultTableModel subModel;
    JTable courseTable;

    public CourseWorkJPanel(Business b, StudentProfile sp, JPanel clp) {
        business = b;
        student = sp;
        CardSequencePanel = clp;
        setLayout(new BorderLayout(10, 10));

        JLabel title = new JLabel("My Course Work - " + (b.getCurrentSemester() == null ? "(no semester set)" : b.getCurrentSemester()));
        title.setFont(new java.awt.Font("Dialog", java.awt.Font.BOLD, 20));
        add(title, BorderLayout.NORTH);

        courseModel = new DefaultTableModel(new Object[]{"Seat", "Course #", "Name", "Credits", "Grade", "Assignments Submitted"}, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        subModel = new DefaultTableModel(new Object[]{"Course #", "Assignment", "Submitted At"}, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        courseTable = new JTable(courseModel);
        courseTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        courseTable.getColumnModel().getColumn(0).setMinWidth(0);
        courseTable.getColumnModel().getColumn(0).setMaxWidth(0);

        JPanel center = new JPanel(new java.awt.GridLayout(2, 1, 10, 10));
        JScrollPane s1 = new JScrollPane(courseTable);
        s1.setBorder(BorderFactory.createTitledBorder("Current courses and progress"));
        JScrollPane s2 = new JScrollPane(new JTable(subModel));
        s2.setBorder(BorderFactory.createTitledBorder("My submissions"));
        center.add(s1);
        center.add(s2);
        add(center, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton back = new JButton("<< Back");
        JButton submit = new JButton("Submit Assignment...");
        back.addActionListener(e -> {
            CardSequencePanel.remove(this);
            ((CardLayout) CardSequencePanel.getLayout()).last(CardSequencePanel);
        });
        submit.addActionListener(e -> submitAssignment());
        bottom.add(back);
        bottom.add(submit);
        add(bottom, BorderLayout.SOUTH);

        refresh();
    }

    private CourseLoad currentLoad() {
        String s = business.getCurrentSemester();
        if (s == null || student.getAcademicProfile() == null) {
            return null;
        }
        return student.getAcademicProfile().getCourseLoadBySemester(s);
    }

    private void refresh() {
        courseModel.setRowCount(0);
        subModel.setRowCount(0);
        CourseLoad cl = currentLoad();
        if (cl != null) {
            for (SeatAssignment sa : cl.getSeatAssignments()) {
                String cn = sa.getCourseOffer().getCourseNumber();
                float g = sa.getGrade();
                courseModel.addRow(new Object[]{sa, cn, sa.getAssociatedCourse().getName(), sa.getCreditHours(),
                    GradeScale.isGraded(g) ? GradeScale.toLetter(g) : "In progress",
                    student.getSubmissionCount(cn)});
            }
        }
        for (String[] s : student.getSubmissions()) {
            subModel.addRow(s);
        }
    }

    private void submitAssignment() {
        int row = courseTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a course first.", "No selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String cn = (String) courseModel.getValueAt(row, 1);
        String name = JOptionPane.showInputDialog(this, "Assignment name for " + cn + ":");
        if (name == null) {
            return; //cancelled
        }
        name = name.trim();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Assignment name cannot be blank.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String when = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        student.addSubmission(cn, name, when);
        refresh();
        JOptionPane.showMessageDialog(this, "Submitted \"" + name + "\" for " + cn + ".");
    }
}
