// Guy-robert Bogning
// March 18, 2026
// Implements an OCCC person, extending RegisteredPerson

public class OCCCPerson extends RegisteredPerson {

    private String studentID;

    public OCCCPerson(String firstName, String lastName, String studentID) {
        super(firstName, lastName, studentID);
        this.studentID = studentID;
    }

    public OCCCPerson(RegisteredPerson p, String studentID) {
        super(p);
        this.studentID = studentID;
    }

    public OCCCPerson(OCCCPerson p) {
        super(p);
        this.studentID = p.studentID;
    }

    public String getStudentID() {
        return studentID;
    }

    public boolean equals(OCCCPerson p) {
        return super.equals(p) && studentID.equals(p.studentID);
    }

    public boolean equals(RegisteredPerson p) {
        return super.equals(p);
    }

    public boolean equals(Person p) {
        return super.equals(p);
    }

    public String toString() {
        return super.toString() + " Student ID: " + studentID;
    }
}
