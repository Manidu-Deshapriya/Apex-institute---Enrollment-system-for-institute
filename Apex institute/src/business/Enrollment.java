package business;

public class Enrollment {
    private String studentID;
    private String fullName;
    private String nic;
    private String courseCode;
    private double gpa;
    private String district;
    private boolean isEnrolled;
    private String dob;

    public Enrollment() {
    }

    public Enrollment(String studentID, String fullName, String nic, String courseCode, double gpa, String district,
            boolean isEnrolled, String dob) {
        this.studentID = studentID;
        this.fullName = fullName;
        this.nic = nic;
        this.courseCode = courseCode;
        this.gpa = gpa;
        this.district = district;
        this.isEnrolled = isEnrolled;
        this.dob = dob;
    }

    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public double getGpa() {
        return gpa;
    }

    public void setGpa(double gpa) {
        this.gpa = gpa;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public boolean isEnrolled() {
        return isEnrolled;
    }

    public void setEnrolled(boolean enrolled) {
        isEnrolled = enrolled;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    @Override
    public String toString() {
        return studentID + " - " + fullName + " (" + courseCode + ")";
    }
}
