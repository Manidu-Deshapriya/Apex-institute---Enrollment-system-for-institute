package user;

import business.Enrollment;
import data.enrollDB;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class EnrollmentUI extends javax.swing.JFrame {

    private enrollDB eDB;

    private final Color COLOR_BG = new Color(30, 30, 30);
    private final Color COLOR_PANEL = new Color(45, 45, 48);
    private final Color COLOR_ACCENT = new Color(0, 122, 204);
    private final Color COLOR_TEXT = new Color(240, 240, 240);
    private final Color COLOR_TEXT_DIM = new Color(180, 180, 180);
    private final Font FONT_HEADER = new Font("Segoe UI", Font.BOLD, 24);
    private final Font FONT_LABEL = new Font("Segoe UI", Font.BOLD, 14);
    private final Font FONT_INPUT = new Font("Segoe UI", Font.PLAIN, 14);
    private final Font FONT_BUTTON = new Font("Segoe UI", Font.BOLD, 13);

    private final String[] SRI_LANKAN_DISTRICTS = {
            "Ampara", "Anuradhapura", "Badulla", "Batticaloa", "Colombo",
            "Galle", "Gampaha", "Hambantota", "Jaffna", "Kalutara",
            "Kandy", "Kegalle", "Kilinochchi", "Kurunegala", "Mannar",
            "Matale", "Matara", "Moneragala", "Mullaitivu", "Nuwara Eliya",
            "Polonnaruwa", "Puttalam", "Ratnapura", "Trincomalee", "Vavuniya"
    };

    private final String[] COURSE_OPTIONS = {
            "SE - Software Engineering",
            "BM - Business Management",
            "ENG - Engineering",
            "LAW",
            "AME - Automobile Engineering"
    };

    private JTextField txtStudentID, txtName, txtNIC, txtGPA, txtDOB;
    private JComboBox<String> cbCourse, cbDistrict;
    private JTextField txtGlobalSearch;
    private JPopupMenu searchPopup;
    private JList<Enrollment> searchList;
    private DefaultListModel<Enrollment> searchListModel;
    private JButton btnEnrolledStatus;
    private boolean isEnrolledValue = false;
    private JTable tblDisplay;
    private DefaultTableModel tableModel;

    public EnrollmentUI() {
        eDB = new enrollDB();
        initComponents();
        setupTheme();
        refreshTable(eDB.getAll());
        setLocationRelativeTo(null);
    }

    private void setupTheme() {
        getContentPane().setBackground(COLOR_BG);
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }
    }

    private void initComponents() {
        setTitle("Apex Institute - Student Enrollment Management");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new BorderLayout(20, 20));

        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(COLOR_PANEL);
        pnlHeader.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel lblTitle = new JLabel("APEX INSTITUTE Enrollment System");
        lblTitle.setFont(FONT_HEADER);
        lblTitle.setForeground(COLOR_ACCENT);
        pnlHeader.add(lblTitle, BorderLayout.WEST);

        JLabel lblSubtitle = new JLabel("Academic Management Portal");
        lblSubtitle.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblSubtitle.setForeground(COLOR_TEXT_DIM);
        pnlHeader.add(lblSubtitle, BorderLayout.EAST);

        add(pnlHeader, BorderLayout.NORTH);

        JPanel pnlCenter = new JPanel(new BorderLayout(20, 20));
        pnlCenter.setBackground(COLOR_BG);
        pnlCenter.setBorder(new EmptyBorder(0, 20, 20, 20));

        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        pnlSearch.setBackground(COLOR_PANEL);
        pnlSearch.setBorder(new LineBorder(new Color(60, 60, 60)));
        JLabel lblGlobalSearch = new JLabel("Global Search (ID / Name / Course):");
        lblGlobalSearch.setForeground(COLOR_TEXT);
        lblGlobalSearch.setFont(FONT_LABEL);
        txtGlobalSearch = createTextField();
        txtGlobalSearch.setPreferredSize(new Dimension(400, 30));
        pnlSearch.add(lblGlobalSearch);
        pnlSearch.add(txtGlobalSearch);

        searchPopup = new JPopupMenu();
        searchListModel = new DefaultListModel<>();
        searchList = new JList<>(searchListModel);
        searchList.setBackground(COLOR_PANEL);
        searchList.setForeground(COLOR_TEXT);
        searchList.setFont(FONT_INPUT);
        searchList.setSelectionBackground(COLOR_ACCENT);
        searchPopup.add(new JScrollPane(searchList));
        searchPopup.setFocusable(false);

        txtGlobalSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent e) {
                updateSearchPopup();
            }
        });

        searchList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    Enrollment selected = searchList.getSelectedValue();
                    if (selected != null) {
                        populateFieldsFromBean(selected);
                        searchPopup.setVisible(false);
                    }
                }
            }
        });

        JPanel pnlTop = new JPanel(new BorderLayout(10, 10));
        pnlTop.setBackground(COLOR_BG);
        pnlTop.add(pnlSearch, BorderLayout.NORTH);

        JPanel pnlInput = new JPanel(new GridBagLayout());
        pnlInput.setBackground(COLOR_PANEL);
        pnlInput.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(60, 60, 60), 1),
                new EmptyBorder(20, 20, 20, 20)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        addInput(pnlInput, "Student ID:", txtStudentID = createTextField(), gbc, 0, 0);
        addInput(pnlInput, "Full Name:", txtName = createTextField(), gbc, 2, 0);
        addInput(pnlInput, "NIC Number:", txtNIC = createTextField(), gbc, 0, 1);
        addInput(pnlInput, "Course Code:", cbCourse = createComboBox(COURSE_OPTIONS), gbc, 2, 1);
        addInput(pnlInput, "GPA:", txtGPA = createTextField(), gbc, 0, 2);
        addInput(pnlInput, "District:", cbDistrict = createComboBox(SRI_LANKAN_DISTRICTS), gbc, 2, 2);
        addInput(pnlInput, "DOB (M/d/yyyy):", txtDOB = createTextField(), gbc, 0, 3);
        txtDOB.setText("01/01/2000");

        btnEnrolledStatus = new JButton("Not Enrolled");
        btnEnrolledStatus.setFont(FONT_BUTTON);
        btnEnrolledStatus.setFocusPainted(false);
        btnEnrolledStatus.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        btnEnrolledStatus.setCursor(new Cursor(Cursor.HAND_CURSOR));
        setEnrolledButtonState(false);

        btnEnrolledStatus.addActionListener(e -> {
            isEnrolledValue = !isEnrolledValue;
            setEnrolledButtonState(isEnrolledValue);
        });

        gbc.gridx = 2;
        gbc.gridy = 3;
        pnlInput.add(btnEnrolledStatus, gbc);

        pnlTop.add(pnlInput, BorderLayout.SOUTH);
        pnlCenter.add(pnlTop, BorderLayout.NORTH);

        String[] columns = { "Student ID", "Name", "NIC", "Course", "GPA", "District", "Enrolled", "DOB" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblDisplay = new JTable(tableModel);
        styleTable(tblDisplay);

        JScrollPane scrollPane = new JScrollPane(tblDisplay);
        scrollPane.getViewport().setBackground(COLOR_BG);
        scrollPane.setBorder(new LineBorder(new Color(60, 60, 60)));
        pnlCenter.add(scrollPane, BorderLayout.CENTER);

        add(pnlCenter, BorderLayout.CENTER);

        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        pnlButtons.setBackground(COLOR_BG);

        pnlButtons.add(createButton("Add Record", e -> addRecord()));
        pnlButtons.add(createButton("Update Record", e -> updateRecord()));
        pnlButtons.add(createButton("Delete Record", e -> deleteRecord()));
        pnlButtons.add(createButton("Sort List", e -> sortRecords()));
        pnlButtons.add(createButton("Clear / Refresh", e -> {
            clearFields();
            refreshTable(eDB.getAll());
        }));

        add(pnlButtons, BorderLayout.SOUTH);

        pack();

        tblDisplay.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tblDisplay.getSelectedRow() != -1) {
                int row = tblDisplay.getSelectedRow();
                populateFieldsFromRow(row);
            }
        });
    }

    private void addInput(JPanel panel, String label, JComponent comp, GridBagConstraints gbc, int x, int y) {
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.weightx = 0;
        JLabel lbl = new JLabel(label);
        lbl.setForeground(COLOR_TEXT);
        lbl.setFont(FONT_LABEL);
        panel.add(lbl, gbc);

        gbc.gridx = x + 1;
        gbc.weightx = 1.0;
        panel.add(comp, gbc);
    }

    private JTextField createTextField() {
        JTextField tf = new JTextField();
        tf.setBackground(new Color(60, 60, 60));
        tf.setForeground(Color.WHITE);
        tf.setCaretColor(Color.WHITE);
        tf.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        tf.setFont(FONT_INPUT);
        return tf;
    }

    private JComboBox<String> createComboBox(String[] items) {
        JComboBox<String> cb = new JComboBox<>(items);
        cb.setBackground(new Color(60, 60, 60));
        cb.setForeground(Color.WHITE);
        cb.setFont(FONT_INPUT);
        cb.setEditable(true);

        cb.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
                    boolean cellHasFocus) {
                JLabel l = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                l.setBackground(isSelected ? COLOR_ACCENT : new Color(60, 60, 60));
                l.setForeground(Color.WHITE);
                return l;
            }
        });

        if (items == SRI_LANKAN_DISTRICTS) {
            JTextField editor = (JTextField) cb.getEditor().getEditorComponent();
            editor.setBackground(new Color(60, 60, 60));
            editor.setForeground(Color.WHITE);
            editor.setCaretColor(Color.WHITE);
            editor.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyReleased(java.awt.event.KeyEvent e) {
                    if (e.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER
                            || e.getKeyCode() == java.awt.event.KeyEvent.VK_UP
                            || e.getKeyCode() == java.awt.event.KeyEvent.VK_DOWN)
                        return;
                    String text = editor.getText();
                    cb.hidePopup();
                    DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
                    for (String d : SRI_LANKAN_DISTRICTS) {
                        if (d.toLowerCase().contains(text.toLowerCase()))
                            model.addElement(d);
                    }
                    if (model.getSize() > 0) {
                        cb.setModel(model);
                        editor.setText(text);
                        cb.showPopup();
                    }
                }
            });
        }
        return cb;
    }

    private void updateSearchPopup() {
        String query = txtGlobalSearch.getText().trim();
        searchListModel.clear();
        if (query.length() < 1) {
            searchPopup.setVisible(false);
            return;
        }

        ArrayList<Enrollment> results = eDB.search(query);
        for (Enrollment e : results) {
            searchListModel.addElement(e);
        }

        if (searchListModel.size() > 0) {
            searchPopup.show(txtGlobalSearch, 0, txtGlobalSearch.getHeight());
            searchList.setSelectedIndex(0);
            txtGlobalSearch.requestFocus();
        } else {
            searchPopup.setVisible(false);
        }
    }

    private void setEnrolledButtonState(boolean enrolled) {
        this.isEnrolledValue = enrolled;
        if (enrolled) {
            btnEnrolledStatus.setText("Enrolled (TRUE)");
            btnEnrolledStatus.setBackground(COLOR_ACCENT);
            btnEnrolledStatus.setForeground(Color.WHITE);
        } else {
            btnEnrolledStatus.setText("Not Enrolled (FALSE)");
            btnEnrolledStatus.setBackground(new Color(60, 60, 60));
            btnEnrolledStatus.setForeground(COLOR_TEXT_DIM);
        }
    }

    private JButton createButton(String text, java.awt.event.ActionListener l) {
        JButton btn = new JButton(text);
        btn.setFont(FONT_BUTTON);
        btn.setBackground(COLOR_ACCENT);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(COLOR_ACCENT.brighter());
            }

            public void mouseExited(MouseEvent e) {
                btn.setBackground(COLOR_ACCENT);
            }
        });

        btn.addActionListener(l);
        return btn;
    }

    private void styleTable(JTable table) {
        table.setBackground(new Color(60, 60, 60));
        table.setForeground(COLOR_TEXT);
        table.setGridColor(new Color(80, 80, 80));
        table.setRowHeight(25);
        table.setFont(FONT_INPUT);
        table.setSelectionBackground(COLOR_ACCENT);
        table.setSelectionForeground(Color.WHITE);

        JTableHeader header = table.getTableHeader();
        header.setBackground(COLOR_PANEL);
        header.setForeground(COLOR_TEXT);
        header.setFont(FONT_BUTTON);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(80, 80, 80)));

        ((DefaultTableCellRenderer) table.getDefaultRenderer(Object.class)).setOpaque(true);
    }

    private void addRecord() {
        try {
            Enrollment e = getFromFields();
            if (eDB.add(e)) {
                JOptionPane.showMessageDialog(this, "Record Added Successfully!");
                refreshTable(eDB.getAll());
                clearFields();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateRecord() {
        try {
            Enrollment e = getFromFields();
            if (eDB.update(e)) {
                JOptionPane.showMessageDialog(this, "Record Updated Successfully!");
                refreshTable(eDB.getAll());
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this, "Student ID not found. Cannot update.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void deleteRecord() {
        String id = txtStudentID.getText();
        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a Student ID to delete.");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete ID: " + id + "?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            if (eDB.delete(id)) {
                JOptionPane.showMessageDialog(this, "Record Deleted.");
                refreshTable(eDB.getAll());
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this, "Record not found.");
            }
        }
    }

    private void sortRecords() {
        String[] options = { "Name", "Course Code", "GPA" };
        int choice = JOptionPane.showOptionDialog(this, "Select Sorting Criteria:", "Sort Records",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        if (choice >= 0) {
            String criteria = choice == 0 ? "name" : (choice == 1 ? "course" : "gpa");
            eDB.sort(criteria);
            refreshTable(eDB.getAll());
        }
    }

    private void refreshTable(ArrayList<Enrollment> list) {
        tableModel.setRowCount(0);
        for (Enrollment e : list) {
            tableModel.addRow(new Object[] {
                    e.getStudentID(), e.getFullName(), e.getNic(), e.getCourseCode(),
                    e.getGpa(), e.getDistrict(), e.isEnrolled() ? "TRUE" : "FALSE", e.getDob()
            });
        }
    }

    private void clearFields() {
        txtStudentID.setText("");
        txtName.setText("");
        txtNIC.setText("");
        txtGPA.setText("");
        cbDistrict.setSelectedIndex(0);
        cbCourse.setSelectedIndex(0);
        txtDOB.setText("01/01/2000");
        setEnrolledButtonState(false);
        tblDisplay.clearSelection();
        txtGlobalSearch.setText("");
    }

    private Enrollment getFromFields() {
        String id = txtStudentID.getText().trim();
        String name = txtName.getText().trim();
        String nic = txtNIC.getText().trim();
        String course = (String) cbCourse.getSelectedItem();

        if (nic.length() > 12) {
            throw new IllegalArgumentException("NIC Number must be 12 characters or less.");
        }

        if (id.isEmpty())
            throw new IllegalArgumentException("Student ID is required.");
        if (name.isEmpty())
            throw new IllegalArgumentException("Full Name is required.");

        double gpa = 0.0;
        try {
            if (!txtGPA.getText().isEmpty())
                gpa = Double.parseDouble(txtGPA.getText().trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("GPA must be a valid number.");
        }

        String district = (String) cbDistrict.getSelectedItem();
        boolean enrolled = isEnrolledValue;
        String dob = txtDOB.getText().trim();

        return new Enrollment(id, name, nic, course, gpa, district, enrolled, dob);
    }

    private void populateFieldsFromRow(int row) {
        txtStudentID.setText(tblDisplay.getValueAt(row, 0).toString());
        txtName.setText(tblDisplay.getValueAt(row, 1).toString());
        txtNIC.setText(tblDisplay.getValueAt(row, 2).toString());
        String courseVal = tblDisplay.getValueAt(row, 3).toString();
        cbCourse.setSelectedItem(courseVal);
        txtGPA.setText(tblDisplay.getValueAt(row, 4).toString());
        String districtVal = tblDisplay.getValueAt(row, 5).toString();
        cbDistrict.setSelectedItem(districtVal);
        boolean isEnrolled = tblDisplay.getValueAt(row, 6).toString().equalsIgnoreCase("TRUE");
        setEnrolledButtonState(isEnrolled);
        txtDOB.setText(tblDisplay.getValueAt(row, 7).toString());
    }

    private void populateFieldsFromBean(Enrollment e) {
        txtStudentID.setText(e.getStudentID());
        txtName.setText(e.getFullName());
        txtNIC.setText(e.getNic());
        cbCourse.setSelectedItem(e.getCourseCode());
        txtGPA.setText(String.valueOf(e.getGpa()));
        cbDistrict.setSelectedItem(e.getDistrict());
        setEnrolledButtonState(e.isEnrolled());
        txtDOB.setText(e.getDob());
    }

    public static void main(String args[]) {
        SwingUtilities.invokeLater(() -> new EnrollmentUI().setVisible(true));
    }
}
