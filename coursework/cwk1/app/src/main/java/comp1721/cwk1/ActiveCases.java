package comp1721.cwk1;

import java.io.IOException;

public class ActiveCases {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("please enter read file path as first command"
                    + " and a write file path as second command");
            System.exit(0);
        }
        String inputFile = args[0];
        String outputFile = args[1];
        CovidDataset dataset = new CovidDataset();
        try {
            dataset.readDailyCases(inputFile);
            System.out.println("Read " + dataset.size() + " from file: " + inputFile);
            dataset.writeActiveCases(outputFile);
        } catch (IOException e) {
            System.exit(1);
        }
    }
}
