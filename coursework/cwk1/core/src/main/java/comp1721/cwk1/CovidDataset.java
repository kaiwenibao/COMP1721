package comp1721.cwk1;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CovidDataset {
    private List<CaseRecord> records;

    public CovidDataset() {
        records = new ArrayList<>();
    }

    public int size() {
        return records.size();
    }

    public CaseRecord getRecord(int index) {
        if (index < 0 || index > size() - 1) {
            throw new DatasetException("The input index is invalid!");
        }
        return records.get(index);
    }

    public void addRecord(CaseRecord record) {
        records.add(record);
    }

    /**
     * Find a specific day's case record
     * If not exist, throw a DatasetException.
     * @param day
     * @return
     */
    public CaseRecord dailyCasesOn(LocalDate day) {
        return records.stream()
                .filter(record -> record.getDate().isEqual(day))
                .findAny()
                .orElseThrow(() -> {
                    throw new DatasetException("This day has no cases");
                });
    }

    /**
     * Read a giving file's data that contains covid cases
     * @param filename
     * @throws FileNotFoundException
     */
    public void readDailyCases(String filename) throws FileNotFoundException {
        // first clear the existing data
        records.clear();
        BufferedReader reader = new BufferedReader(new FileReader(new File(filename)));
        // skip first line(headings) and read each other line then convert to CaseRecord
        records = reader.lines().skip(1).map(this::convert).collect(Collectors.toList());
    }

    /**
     * Convert one line(except the header line) in the .csv file
     * to a {@link CaseRecord}
     * @param line one line content read from file
     * @return CaseRecord
     */
    private CaseRecord convert(String line) {
        String[] split = line.split(",");
        if (split.length < 4) {
            throw new DatasetException("Missing column in file");
        }
        LocalDate date = LocalDate.parse(split[0], DateTimeFormatter.ISO_LOCAL_DATE);
        int staffCases = Integer.parseInt(split[1]);
        int studentCases = Integer.parseInt(split[2]);
        int otherCases = Integer.parseInt(split[3]);
        return new CaseRecord(date, staffCases, studentCases, otherCases);
    }

    /**
     * Write a summary records to giving file
     * @param filename
     * @throws IOException
     */
    public void writeActiveCases(String filename) throws IOException {
        if (size() < 10) {
            throw new DatasetException("Less than 10 records in dataset!");
        }
        BufferedWriter writer = new BufferedWriter(new FileWriter(new File(filename)));
        // write headings
        writer.write("Date,Staff,Students,Other");
        // split records to small group with each having 10 records
        int group = size() / 10;
        CaseRecord record = null;
        List<CaseRecord> caseRecords = new ArrayList<>();
        for (int i = 0; i < group; i++) {
            List<CaseRecord> subList = records.subList(i * 10, (i + 1) * 10);
            if (record != null) {
                // summary 10 records as one new record
                CaseRecord newRec = sumRecordsForLastTenDays(subList);
                record = new CaseRecord(newRec.getDate(),
                        newRec.getStaffCases() + record.getStaffCases(),
                        newRec.getStudentCases() + record.getStudentCases(),
                        newRec.getOtherCases() + record.getOtherCases());
            } else {
                record = sumRecordsForLastTenDays(subList);
            }
            // add to new list
            caseRecords.add(record);
        }
        int staffCases = record.getStaffCases();
        int studentCases = record.getStudentCases();
        int otherCases = record.getOtherCases();
        // add last records(that less than 10 in the end)
        records.stream().skip(group * 10)
                .map(rec -> new CaseRecord(rec.getDate(), rec.getStaffCases() + staffCases,
                        rec.getStudentCases() + studentCases, rec.getOtherCases() + otherCases))
                .forEachOrdered(caseRecords::add);
        // read each new records and write them to giving file
        caseRecords.stream()
                .map(rec -> rec.getDate() + "," + rec.getStaffCases() + ","
                        + rec.getStudentCases() + "," + rec.getOtherCases())
                .forEachOrdered(rec -> {
                    try {
                        writer.newLine();
                        writer.write(rec);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
        writer.flush();
    }

    /**
     * summary ten records to one new record
     * @param cases
     * @return
     */
    private CaseRecord sumRecordsForLastTenDays(List<CaseRecord> cases) {
        LocalDate date = cases.get(cases.size() - 1).getDate();
        int staffCases = cases.stream().mapToInt(CaseRecord::getStaffCases).sum();
        int studentCases = cases.stream().mapToInt(CaseRecord::getStudentCases).sum();
        int otherCases = cases.stream().mapToInt(CaseRecord::getOtherCases).sum();
        return new CaseRecord(date, staffCases, studentCases, otherCases);
    }
}
