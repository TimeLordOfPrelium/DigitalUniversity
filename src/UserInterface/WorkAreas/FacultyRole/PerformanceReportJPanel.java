package UserInterface.WorkAreas.FacultyRole;

import Business.Business;
import Business.Faculty.TeachingRoster;
import Business.Profiles.FacultyProfile;
import Business.Profiles.StudentProfile;
import Business.UserAccounts.UserAccount;
import info5100.university.example.CourseSchedule.CourseOffer;
import info5100.university.example.CourseSchedule.CourseOfferSummary;
import info5100.university.example.CourseSchedule.GradeScale;
import info5100.university.example.CourseSchedule.SeatAssignment;
import java.util.Map;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

/**
 * Faculty use case: Generate performance reports (student grades in the class).
 * Summarizes every class the faculty member taught in a semester, then shows the
 * student grades and grade distribution for the class they select.
 *
 * @author Samuel Haddad
 */
public class PerformanceReportJPanel extends javax.swing.JPanel {

    JPanel CardSequencePanel;
    Business business;
    FacultyProfile faculty;
    boolean loading;   //true while the semester list is being filled, so its events are ignored

    public PerformanceReportJPanel(Business b, UserAccount ua, JPanel clp) {
        business = b;
        faculty = (FacultyProfile) ua.getAssociatedPersonProfile();
        CardSequencePanel = clp;
        initComponents();
        tblSummary.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        //picking a class in the summary shows its student grades and grade spread below
        tblSummary.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                showClassDetail();
            }
        });

        populateSemesters();
        refreshSummary();
    }

    //semesters this faculty member taught in; the current semester is selected by default
    private void populateSemesters() {
        loading = true;
        cmbSemester.removeAllItems();
        for (String semester : TeachingRoster.getSemesters(business, faculty)) {
            cmbSemester.addItem(semester);
        }
        if (business.getCurrentSemester() != null) {
            cmbSemester.setSelectedItem(business.getCurrentSemester());
        }
        loading = false;
    }

    //one row per class taught in the selected semester
    private void refreshSummary() {
        DefaultTableModel model = (DefaultTableModel) tblSummary.getModel();
        model.setRowCount(0);
        clearDetail();

        Object semester = cmbSemester.getSelectedItem();
        if (semester == null) {
            return; //this faculty member has no classes yet
        }
        for (CourseOffer co : TeachingRoster.getCourseOffers(business, faculty, semester.toString())) {
            CourseOfferSummary summary = new CourseOfferSummary(co);
            Object[] row = new Object[6];
            row[0] = summary; //the summary itself, so a selected row gives it back
            row[1] = summary.getEnrolled();
            row[2] = summary.getGraded();
            row[3] = formatAverage(summary);
            row[4] = summary.getGraded() == 0 ? "-" : GradeScale.toLetter(summary.getHighest());
            row[5] = summary.getGraded() == 0 ? "-" : GradeScale.toLetter(summary.getLowest());
            model.addRow(row);
        }
    }

    private String formatAverage(CourseOfferSummary summary) {
        if (summary.getGraded() == 0) {
            return "No grades yet";
        }
        return String.format("%.2f (%s)", summary.getAverage(), GradeScale.toLetter(summary.getAverage()));
    }

    private void clearDetail() {
        ((DefaultTableModel) tblGrades.getModel()).setRowCount(0);
        ((DefaultTableModel) tblDistribution.getModel()).setRowCount(0);
        lblDetail.setText("Select a class above to see each student's grade.");
    }

    //fills the student grade list and the grade distribution for the selected class
    private void showClassDetail() {
        int selectedrow = tblSummary.getSelectedRow();
        if (selectedrow < 0) {
            clearDetail();
            return;
        }
        CourseOfferSummary summary = (CourseOfferSummary) tblSummary.getValueAt(selectedrow, 0);
        CourseOffer co = summary.getCourseOffer();
        lblDetail.setText("Student grades for " + co + ":  average " + formatAverage(summary));

        DefaultTableModel grades = (DefaultTableModel) tblGrades.getModel();
        grades.setRowCount(0);
        for (SeatAssignment sa : TeachingRoster.getEnrolledSeats(co)) {
            StudentProfile sp = TeachingRoster.findStudent(business, sa);
            if (sp == null) {
                continue;
            }
            Object[] row = new Object[4];
            row[0] = sp.getPerson().getFullName();
            row[1] = sp.getPerson().getPersonId();
            row[2] = GradeScale.toLetter(sa.getGrade());
            row[3] = GradeScale.isGraded(sa.getGrade()) ? String.format("%.1f", sa.getGrade()) : "-";
            grades.addRow(row);
        }

        DefaultTableModel spread = (DefaultTableModel) tblDistribution.getModel();
        spread.setRowCount(0);
        for (Map.Entry<String, Integer> entry : summary.getDistribution().entrySet()) {
            if (entry.getValue() > 0) {
                spread.addRow(new Object[]{entry.getKey(), entry.getValue()});
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblTitle = new javax.swing.JLabel();
        lblSemester = new javax.swing.JLabel();
        cmbSemester = new javax.swing.JComboBox<>();
        lblSummary = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblSummary = new javax.swing.JTable();
        lblDetail = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblGrades = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblDistribution = new javax.swing.JTable();
        btnBack = new javax.swing.JButton();

        setBackground(new java.awt.Color(0, 153, 153));
        setLayout(null);

        lblTitle.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        lblTitle.setText("Performance Reports");
        add(lblTitle);
        lblTitle.setBounds(21, 20, 400, 29);

        lblSemester.setText("Semester:");
        add(lblSemester);
        lblSemester.setBounds(30, 62, 80, 25);

        cmbSemester.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbSemesterActionPerformed(evt);
            }
        });
        add(cmbSemester);
        cmbSemester.setBounds(110, 60, 180, 28);

        lblSummary.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        lblSummary.setText("Class summary");
        add(lblSummary);
        lblSummary.setBounds(30, 98, 300, 20);

        tblSummary.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Course", "Enrolled", "Graded", "Class Average", "Highest", "Lowest"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblSummary);

        add(jScrollPane1);
        jScrollPane1.setBounds(30, 120, 740, 105);

        lblDetail.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        lblDetail.setText("Select a class above to see each student's grade.");
        add(lblDetail);
        lblDetail.setBounds(30, 235, 740, 20);

        tblGrades.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Student", "Student ID", "Grade", "Grade Points"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(tblGrades);

        add(jScrollPane2);
        jScrollPane2.setBounds(30, 258, 480, 135);

        tblDistribution.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Grade", "Students"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(tblDistribution);

        add(jScrollPane3);
        jScrollPane3.setBounds(530, 258, 240, 135);

        btnBack.setText("<< Back");
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });
        add(btnBack);
        btnBack.setBounds(30, 405, 90, 32);
    }// </editor-fold>//GEN-END:initComponents

    private void cmbSemesterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSemesterActionPerformed
        if (!loading) {
            refreshSummary();
        }
    }//GEN-LAST:event_cmbSemesterActionPerformed

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        CardSequencePanel.remove(this);
        ((java.awt.CardLayout) CardSequencePanel.getLayout()).last(CardSequencePanel); //show the screen underneath
    }//GEN-LAST:event_btnBackActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
    private javax.swing.JComboBox<Object> cmbSemester;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lblDetail;
    private javax.swing.JLabel lblSemester;
    private javax.swing.JLabel lblSummary;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JTable tblDistribution;
    private javax.swing.JTable tblGrades;
    private javax.swing.JTable tblSummary;
    // End of variables declaration//GEN-END:variables
}
