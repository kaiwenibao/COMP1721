package comp1721.cwk1;

import java.time.LocalDate;

public class CaseRecord {

    private LocalDate date;
    private int staffCases;
    private int studentCases;
    private int otherCases;

    public CaseRecord(LocalDate date, int staffCases, int studentCases, int otherCases) {
        if (staffCases < 0 || studentCases < 0 || otherCases < 0) {
            throw new DatasetException("Illegal Argument with negative value!");
        }
        this.date = date;
        this.staffCases = staffCases;
        this.studentCases = studentCases;
        this.otherCases = otherCases;
    }

    public LocalDate getDate() {
        return date;
    }

    public int getStaffCases() {
        return staffCases;
    }

    public int getStudentCases() {
        return studentCases;
    }

    public int getOtherCases() {
        return otherCases;
    }

    public int totalCases() {
        return staffCases + studentCases + otherCases;
    }

    @Override
    public String toString() {
        return date + ": " + staffCases + " staff, "
                + studentCases + " students, "
                + otherCases + " other";
    }

}
