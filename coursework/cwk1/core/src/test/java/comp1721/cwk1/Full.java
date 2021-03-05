// Correctness testing for COMP1721 Coursework 1 (Full Solution)

package comp1721.cwk1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Full {
  private final String filename = "build/test-active.csv";

  private CovidDataset dataset;
  private CovidDataset dataset2;  // big enough to compute active cases
  private CovidDataset dataset3;  // not big enough to compute active cases

  @BeforeEach
  public void perTestSetup() {
    dataset = new CovidDataset();
    dataset2 = new CovidDataset();
    dataset3 = new CovidDataset();

    LocalDate date = LocalDate.of(2021, 1, 1);
    CaseRecord rec = new CaseRecord(date, 0, 0, 0);
    dataset2.addRecord(rec);
    for (int i = 1; i < 11; ++i) {
      rec = new CaseRecord(date.plusDays(i), 1, 1, 1);
      dataset2.addRecord(rec);
    }

    for (int i = 0; i < 9; ++i) {
      rec = new CaseRecord(date.plusDays(i), 1, 1, 1);
      dataset3.addRecord(rec);
    }
  }

  @Test
  @DisplayName("Daily cases are read from CSV file")
  public void readDailyCases() throws IOException {
    //                         ^^^^^^^^^^^^^^^^^^
    // Note: this allows for both Scanner-based implementations
    // that throw only FileNotFoundException AND lower-level
    // implementations using BufferedReader
    dataset.readDailyCases("../datafiles/testing/valid.csv");
    assertAll(
      () -> assertThat(dataset.size(), is(3)),
      () -> assertThat(dataset.getRecord(0).getDate().toString(), is("2020-10-17")),
      () -> assertThat(dataset.getRecord(0).getStudentCases(), is(14))
    );
  }

  @Test
  @DisplayName("Reading deletes existing data, even if file has col headings only")
  public void readDailyCasesNoData() throws IOException {
    LocalDate date = LocalDate.of(2021, 1,1);
    CaseRecord rec = new CaseRecord(date, 1, 1, 1);
    dataset.addRecord(rec);
    int initialSize = dataset.size();

    dataset.readDailyCases("../datafiles/testing/headings-only.csv");

    assertAll(
      () -> assertThat(initialSize, is(1)),
      () -> assertThat(dataset.size(), is(0))
    );
  }

  @Test
  @DisplayName("FileNotFoundException if CSV file cannot be found")
  public void readDailyCasesMissingFile() {
    assertThrows(FileNotFoundException.class,
      () -> dataset.readDailyCases("non-existent-file.csv"));
  }

  @Test
  @DisplayName("DatasetException if a column is missing in CSV")
  public void readDailyCasesMissingColumn() {
    assertThrows(DatasetException.class,
      () -> dataset.readDailyCases("../datafiles/testing/missing-column.csv"));
  }

  @Test
  @DisplayName("Active cases written to a CSV file correctly")
  public void writeActiveCases() throws IOException {
    dataset2.writeActiveCases(filename);
    Path path = FileSystems.getDefault().getPath(filename);
    assertTrue(Files.exists(path));

    List<String> lines = Files.readAllLines(path);

    assertAll(
      () -> assertThat(lines.size(), is(3)),
      () -> assertThat(lines.get(0), is("Date,Staff,Students,Other")),
      () -> assertThat(lines.get(1), is("2021-01-10,9,9,9")),
      () -> assertThat(lines.get(2), is("2021-01-11,10,10,10"))
    );
  }

  @Test
  @DisplayName("DatasetException if dataset too small to write active cases")
  public void writeActiveCasesDatasetTooSmall() throws IOException {
    assertThrows(DatasetException.class, () -> dataset3.writeActiveCases(filename));
  }
}
