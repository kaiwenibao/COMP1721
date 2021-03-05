// Correctness testing for COMP1721 Coursework 1 (Basic Solution)

package comp1721.cwk1;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;


public class Basic {
  private LocalDate day1;
  private LocalDate day2;
  private CaseRecord rec1;
  private CaseRecord rec2;
  private CovidDataset dataset;

  @BeforeEach
  public void perTestSetup() {
    day1 = LocalDate.of(2020, 9, 28);
    day2 = LocalDate.of(2020, 10, 19);

    rec1 = new CaseRecord(day1, 1, 50, 0);
    rec2 = new CaseRecord(day2, 3, 21, 1);

    dataset = new CovidDataset();
  }

  // CaseRecord class

  @Test
  @DisplayName("date field set up correctly")
  public void dateField() {
    assertAll(
      () -> assertThat(rec1.getDate(), is(day1)),
      () -> assertThat(rec2.getDate(), is(day2))
    );
  }

  @Test
  @DisplayName("staffCases field set up correctly")
  public void staffCasesField() {
    assertAll(
      () -> assertThat(rec1.getStaffCases(), is(1)),
      () -> assertThat(rec2.getStaffCases(), is(3))
    );
  }

  @Test
  @DisplayName("studentCases field set up correctly")
  public void studentCasesField() {
    assertAll(
      () -> assertThat(rec1.getStudentCases(), is(50)),
      () -> assertThat(rec2.getStudentCases(), is(21))
    );
  }

  @Test
  @DisplayName("otherCases field set up correctly")
  public void otherCasesField() {
    assertAll(
      () -> assertThat(rec1.getOtherCases(), is(0)),
      () -> assertThat(rec2.getOtherCases(), is(1))
    );
  }

  @Test
  @DisplayName("DataException thrown for invalid case counts")
  public void caseCountValidation() {
    assertAll(
      () -> assertThrows(DatasetException.class, () -> new CaseRecord(day1, -1, 0, 0)),
      () -> assertThrows(DatasetException.class, () -> new CaseRecord(day1, 0, -1, 0)),
      () -> assertThrows(DatasetException.class, () -> new CaseRecord(day1, 0, 0, -1))
    );
  }

  @Test
  @DisplayName("Daily case totals computed correctly")
  public void totalCases() {
    assertAll(
      () -> assertThat(rec1.totalCases(), is(51)),
      () -> assertThat(rec2.totalCases(), is(25))
    );
  }

  @Test
  @DisplayName("Correct format for string conversion")
  public void stringConversion() {
    assertThat(rec2.toString(), is("2020-10-19: 3 staff, 21 students, 1 other"));
  }

  // CovidDataset class

  @Test
  @DisplayName("Dataset is initially empty")
  public void datasetInitiallyEmpty() {
    assertThat(dataset.size(), is(0));
  }

  @Test
  @DisplayName("Records can be added to a dataset")
  public void addRecord() {
    dataset.addRecord(rec1);
    assertThat(dataset.size(), is(1));
  }

  @Test
  @DisplayName("Records can be retrieved correctly given their index")
  public void getRecordByIndex() {
    dataset.addRecord(rec1);
    dataset.addRecord(rec2);
    assertAll(
      () -> assertThat(dataset.getRecord(0).getDate(), is(day1)),
      () -> assertThat(dataset.getRecord(1).getDate(), is(day2))
    );
  }

  @Test
  @DisplayName("DataException if using invalid index to retrieve a record")
  public void exceptionForInvalidIndex() {
    dataset.addRecord(rec1);
    dataset.addRecord(rec2);
    assertAll(
      () -> assertThrows(DatasetException.class, () -> dataset.getRecord(-1)),
      () -> assertThrows(DatasetException.class, () -> dataset.getRecord(2))
    );
  }

  @Test
  @DisplayName("Records can be found by date")
  public void findRecord() {
    dataset.addRecord(rec1);
    dataset.addRecord(rec2);
    CaseRecord found = dataset.dailyCasesOn(day2);
    assertAll(
      () -> assertThat(found.getDate(), is(day2)),
      () -> assertThat(found.getStaffCases(), is(rec2.getStaffCases())),
      () -> assertThat(found.getStudentCases(), is(rec2.getStudentCases())),
      () -> assertThat(found.getOtherCases(), is(rec2.getOtherCases()))
    );
  }

  @Test
  @DisplayName("DataException if no record found for required date")
  public void findRecordException() {
    dataset.addRecord(rec1);
    assertThrows(DatasetException.class, () -> dataset.dailyCasesOn(day2));
  }
}
