package comp1721.cwk1;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

import java.io.FileNotFoundException;

public class CovidChart extends Application {

    private static String filename;

    @Override
    public void start(Stage stage) throws FileNotFoundException {
        stage.setTitle("Line Chart Sample");
        //defining the axes
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Day of Year");
        yAxis.setLabel("Active Cases");
        //creating the chart
        final LineChart<Number, Number> lineChart =
                new LineChart<>(xAxis, yAxis);

        lineChart.setTitle("Active Coronavirus Cases, University of Leeds");
        //defining a series
        XYChart.Series series = new XYChart.Series();
        series.setName(filename);

        CovidDataset dataset = new CovidDataset();
        dataset.readDailyCases(filename);
        for (int i = 0; i < dataset.size(); i++) {
            //populating the series with data
            CaseRecord record = dataset.getRecord(i);
            series.getData().add(
                    new XYChart.Data(record.getDate().getDayOfYear(), record.totalCases()));
        }

        Scene scene  = new Scene(lineChart, 800, 600);
        lineChart.getData().add(series);

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        filename = args[0];
        launch(args);
    }
}
