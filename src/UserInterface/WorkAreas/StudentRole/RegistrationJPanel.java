package UserInterface.WorkAreas.StudentRole;

import Business.Business;
import Business.Profiles.StudentProfile;
import info5100.university.example.CourseSchedule.CourseLoad;
import info5100.university.example.CourseSchedule.CourseOffer;
import info5100.university.example.CourseSchedule.CourseSchedule;
import info5100.university.example.CourseSchedule.SeatAssignment;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
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
 * Student use case: Register for Classes (enroll and drop) in the current semester.
 * Checks: a semester must be set, no duplicate enrollment, a free seat must exist.
 *
 * @author James Russell
 */
public class RegistrationJPanel extends JPanel {

    JPanel CardSequencePanel;
    Business business;
    StudentProfile student;
    DefaultTableModel offeredModel;
    DefaultTableModel myModel;
    JTable offeredTable;
    JTable myTable;
    JLabel creditsLabel;

    public RegistrationJPanel(Business b, StudentProfile sp, JPanel clp) {
        business = b;
        student = sp;
        CardSequencePanel = clp;
        setLayout(new BorderLayout(10, 10));

        JLabel title = new JLabel("Course Registration - " + safeSemester());
        title.setFont(new java.awt.Font("Dialog", java.awt.Font.BOLD, 20));
        add(title, BorderLayout.NORTH);

        offeredModel = readOnlyModel(new Object[]{"Course Offer", "Course # ", "Name", "Credits", "Seats Left"});
        myModel = readOnlyModel(new Object[]{"Seat Assignment", "Course #", "Name", "Credits"});
        offeredTable = new JTable(offeredModel);
        myTable = new JTable(myModel);
        hideFirstColumn(offeredTable);
        hideFirstColumn(myTable);
        offeredTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        myTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JPanel center = new JPanel(new GridLayout(2, 1, 10, 10));
        JScrollPane s1 = new JScrollPane(offeredTable);
        s1.setBorder(BorderFactory.createTitledBorder("Courses offered this semester"));
        JScrollPane s2 = new JScrollPane(myTable);
        s2.setBorder(BorderFactory.createTitledBorder("My enrolled courses"));
        center.add(s1);
        center.add(s2);
        add(center, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton back = new JButton("<< Back");
        JButton enroll = new JButton("Enroll in Selected");
        JButton drop = new JButton("Drop Selected");
        creditsLabel = new JLabel();
        back.addActionListener(e -> {
            CardSequencePanel.remove(this);
            ((CardLayout) CardSequencePanel.getLayout()).last(CardSequencePanel);
        });
        enroll.addActionListener(e -> enrollSelected());
        drop.addActionListener(e -> dropSelected());
        bottom.add(back);
        bottom.add(enroll);
        bottom.add(drop);
        bottom.add(creditsLabel);
        add(bottom, BorderLayout.SOUTH);

        refreshTables();
    }

    private String safeSemester() {
        String s = business.getCurrentSemester();
        return (s == null || s.isEmpty()) ? "(no semester set)" : s;
    }

    private CourseSchedule currentSchedule() {
        String s = business.getCurrentSemester();
        return (s == null) ? null : business.getDepartment().getCourseSchedule(s);
    }

    //the student's course load for the current semester, or null if none yet
    private CourseLoad currentLoad() {
        String s = business.getCurrentSemester();
        if (s == null || student.getAcademicProfile() == null) {
            return null;
        }
        return student.getAcademicProfile().getCourseLoadBySemester(s);
    }

    private void refreshTables() {
        offeredModel.setRowCount(0);
        myModel.setRowCount(0);
        CourseSchedule cs = currentSchedule();
        if (cs != null) {
            for (CourseOffer co : cs.getSchedule()) {
                int left = co.getCapacity() - co.getEnrolledCount();
                offeredModel.addRow(new Object[]{co, co.getCourseNumber(),
                    co.getSubjectCourse().getName(), co.getCreditHours(), left});
            }
        }
        int credits = 0;
        CourseLoad cl = currentLoad();
        if (cl != null) {
            for (SeatAssignment sa : cl.getSeatAssignments()) {
                myModel.addRow(new Object[]{sa, sa.getCourseOffer().getCourseNumber(),
                    sa.getAssociatedCourse().getName(), sa.getCreditHours()});
            }
            credits = cl.getTotalCreditHours();
        }
        creditsLabel.setText("   Credits this semester: " + credits);
    }

    private void enrollSelected() {
        int row = offeredTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a course offer to enroll in.", "No selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (currentSchedule() == null) {
            JOptionPane.showMessageDialog(this, "Registration is closed: no current semester is set.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        CourseOffer co = (CourseOffer) offeredModel.getValueAt(row, 0);
        CourseLoad cl = currentLoad();
        if (cl != null) {
            for (SeatAssignment sa : cl.getSeatAssignments()) {
                if (sa.getCourseOffer() == co) {
                    JOptionPane.showMessageDialog(this, "You are already enrolled in " + co.getCourseNumber() + ".", "Duplicate", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }
        }
        if (co.getEmptySeat() == null) {
            JOptionPane.showMessageDialog(this, co.getCourseNumber() + " is full.", "No seats", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (cl == null) {
            cl = student.getAcademicProfile().newCourseLoad(business.getCurrentSemester());
        }
        if (co.assignEmptySeat(cl) == null) {
            JOptionPane.showMessageDialog(this, "Could not enroll. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        refreshTables();
        JOptionPane.showMessageDialog(this, "Enrolled in " + co.getCourseNumber() + ".");
    }

    private void dropSelected() {
        int row = myTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select one of your courses to drop.", "No selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        SeatAssignment sa = (SeatAssignment) myModel.getValueAt(row, 0);
        int ok = JOptionPane.showConfirmDialog(this, "Drop " + sa.getCourseOffer().getCourseNumber() + "?", "Confirm drop", JOptionPane.YES_NO_OPTION);
        if (ok != JOptionPane.YES_OPTION) {
            return;
        }
        currentLoad().dropSeatAssignment(sa);
        refreshTables();
        JOptionPane.showMessageDialog(this, "Dropped " + sa.getCourseOffer().getCourseNumber() + ".");
    }

    private static DefaultTableModel readOnlyModel(Object[] cols) {
        return new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
    }

    //column 0 holds the object itself so the selected row can be read back
    private static void hideFirstColumn(JTable t) {
        t.getColumnModel().getColumn(0).setMinWidth(0);
        t.getColumnModel().getColumn(0).setMaxWidth(0);
        t.getColumnModel().getColumn(0).setPreferredWidth(0);
    }
}
