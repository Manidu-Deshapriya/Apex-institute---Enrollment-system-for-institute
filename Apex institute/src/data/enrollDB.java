package data;

import business.Enrollment;
import java.io.*;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class enrollDB {

    private class Node {
        Enrollment data;
        Node next;
        Node prev;

        Node(Enrollment data) {
            this.data = data;
        }
    }

    private Node head;
    private Node tail;
    private String resolvedPath = "EnrollmentRecords.txt";

    public enrollDB() {
        head = null;
        tail = null;
        loadData();
    }

    private void loadData() {
        head = null;
        tail = null;
        String[] searchPaths = {
                "EnrollmentRecords.txt", "../EnrollmentRecords.txt", "src/EnrollmentRecords.txt", "bin/EnrollmentRecords.txt", "../../EnrollmentRecords.txt"
        };

        File file = null;
        for (String path : searchPaths) {
            File f = new File(path);
            if (f.exists() && f.isFile()) {
                file = f;
                resolvedPath = path;
                break;
            }
        }

        if (file == null) {
            resolvedPath = "EnrollmentRecords.txt";
            return;
        }

        try (BufferedReader br = new BufferedReader
                (new InputStreamReader(new FileInputStream(file), "UTF-8"))) {
            String line = br.readLine();
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty())
                    continue;

                String[] parts = line.split("\t");
                if (parts.length < 8)
                    parts = line.split("\\s{2,}");

                if (parts.length >= 8) {
                    try {
                        String sId = parts[0].trim();
                        String name = parts[1].trim();
                        String nic = parts[2].trim();
                        String course = parts[3].trim();
                        double gpa = Double.parseDouble(parts[4].trim().replace(",", "."));
                        String dist = parts[5].trim();
                        boolean enrolled = parts[6].trim().equalsIgnoreCase("TRUE")
                                || parts[6].trim().equalsIgnoreCase("1");
                        String dob = parts[7].trim();

                        Enrollment e = new Enrollment(sId, name, nic, course, gpa, dist, enrolled, dob);
                        addToList(e);
                    } catch (Exception ex) {
                    }
                }
            }
        } catch (Exception e) {
        }
    }

    private void addToList(Enrollment en) {
        Node newNode = new Node(en);
        if (head == null) {
            head = tail = newNode;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }
    }

    public void save() {
        try (BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(resolvedPath), "UTF-8"))) {
            bw.write("Student ID\tFull Name\tNIC Number\tCourse Code\tGPA\tDistrict\tIs Enrolled\tDate of Birth");
            bw.newLine();

            Node current = head;
            while (current != null) {
                Enrollment e = current.data;
                String line = String.format("%s\t%s\t%s\t%s\t%.2f\t%s\t%s\t%s",
                        e.getStudentID(), e.getFullName(), e.getNic(), e.getCourseCode(),
                        e.getGpa(), e.getDistrict(), e.isEnrolled() ? "TRUE" : "FALSE", e.getDob());
                bw.write(line);
                bw.newLine();
                current = current.next;
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error saving data: " + e.getMessage());
        }
    }

    public boolean add(Enrollment en) {
        if (getStudent(en.getStudentID()) != null) {
            JOptionPane.showMessageDialog(null, "Student ID already exists!");
            return false;
        }
        addToList(en);
        save();
        return true;
    }

    public boolean delete(String studentID) {
        Node current = head;
        while (current != null) {
            if (current.data.getStudentID().equalsIgnoreCase(studentID)) {
                if (current == head) {
                    head = current.next;
                    if (head != null) {
                        head.prev = null;
                    }
                } else {
                    current.prev.next = current.next;
                }

                if (current == tail) {
                    tail = current.prev;
                    if (tail != null) {
                        tail.next = null;
                    }
                } else {
                    current.next.prev = current.prev;
                }

                save();
                return true;
            }
            current = current.next;
        }
        return false;
    }

    public boolean update(Enrollment en) {
        Node current = head;
        while (current != null) {
            if (current.data.getStudentID().equalsIgnoreCase(en.getStudentID())) {
                current.data = en;
                save();
                return true;
            }
            current = current.next;
        }
        return false;
    }

    public Enrollment getStudent(String id) {
        Node current = head;
        while (current != null) {
            if (current.data.getStudentID().equalsIgnoreCase(id)) {
                return current.data;
            }
            current = current.next;
        }
        return null;
    }

    public ArrayList<Enrollment> search(String query) {
        ArrayList<Enrollment> results = new ArrayList<>();
        Node current = head;
        String q = query.toLowerCase().trim();

        while (current != null) {
            Enrollment e = current.data;
            if (e.getStudentID().toLowerCase().contains(q) ||
                    e.getFullName().toLowerCase().contains(q) ||
                    e.getCourseCode().toLowerCase().contains(q)) {
                results.add(e);
            }
            current = current.next;
        }
        return results;
    }

    public ArrayList<Enrollment> getAll() {
        ArrayList<Enrollment> list = new ArrayList<>();
        Node current = head;
        while (current != null) {
            list.add(current.data);
            current = current.next;
        }
        return list;
    }

    public void sort(String criteria) {
        if (head == null || head.next == null)
            return;

        boolean swapped;
        do {
            swapped = false;
            Node current = head;
            while (current.next != null) {
                boolean condition = false;
                Enrollment e1 = current.data;
                Enrollment e2 = current.next.data;

                if (criteria.equalsIgnoreCase("name")) {
                    condition = e1.getFullName().compareToIgnoreCase(e2.getFullName()) > 0;
                } else if (criteria.equalsIgnoreCase("course")) {
                    condition = e1.getCourseCode().compareToIgnoreCase(e2.getCourseCode()) > 0;
                } else if (criteria.equalsIgnoreCase("gpa")) {
                    condition = e1.getGpa() < e2.getGpa();
                }

                if (condition) {
                    Enrollment temp = current.data;
                    current.data = current.next.data;
                    current.next.data = temp;
                    swapped = true;
                }
                current = current.next;
            }
        } while (swapped);
        save();
    }
}
