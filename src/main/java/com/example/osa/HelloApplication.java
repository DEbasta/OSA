package com.example.osa;

import com.example.lab1.*;
import com.example.lab2.AnalisysOfModeling;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        SecondPage sp = new SecondPage();

        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("X");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Y");

        CategoryAxis x1Axis = new CategoryAxis();
        x1Axis.setLabel("Веса");
        NumberAxis y1Axis = new NumberAxis();
        y1Axis.setLabel("Значения");

        CategoryAxis x2Axis = new CategoryAxis();
        x2Axis.setLabel("Ранги");
        NumberAxis y2Axis = new NumberAxis();
        y2Axis.setLabel("Значения");

        CategoryAxis x3Axis = new CategoryAxis();
        x3Axis.setLabel("Веса");
        NumberAxis y3Axis = new NumberAxis();
        y3Axis.setLabel("Значения");

        CategoryAxis x4Axis = new CategoryAxis();
        x4Axis.setLabel("Ранги");
        NumberAxis y4Axis = new NumberAxis();
        y4Axis.setLabel("Значения");

        CategoryAxis x5Axis = new CategoryAxis();
        x4Axis.setLabel("Коэфф");
        NumberAxis y5Axis = new NumberAxis();
        y4Axis.setLabel("Способ");


        LineChart<Number, Number> lineChart0 = new LineChart<>(xAxis, yAxis);
        lineChart0.setTitle("Отрезки");
        XYChart.Series<Number, Number> cut1 = new XYChart.Series<>();
        cut1.setName("Отрезок 1");
        XYChart.Series<Number, Number> cut2 = new XYChart.Series<>();
        cut2.setName("Отрезок 2");
        XYChart.Series<Number, Number> cut3 = new XYChart.Series<>();
        cut3.setName("Отрезок 3");
        XYChart.Series<Number, Number> cut4 = new XYChart.Series<>();
        cut4.setName("Отрезок 4");
        XYChart.Series<Number, Number> cut5 = new XYChart.Series<>();
        cut5.setName("Отрезок 5");
        XYChart.Series<Number, Number> cut6 = new XYChart.Series<>();
        cut6.setName("Отрезок 6");
        XYChart.Series<Number, Number> cut7 = new XYChart.Series<>();
        cut7.setName("Отрезок 7");

        BarChart<String,Number> barChart1 = new BarChart<>(x1Axis,y1Axis);
        barChart1.setTitle("Веса объектов (нормированные), вычесленные по ДИСКРЕТНОЙ шкале");
        barChart1.setCategoryGap(10);
        barChart1.setBarGap(0);

        BarChart<String,Number> barChart2 = new BarChart<>(x2Axis,y2Axis);
        barChart2.setTitle("Обратные ранги объектов, вычесленные по ДИСКРЕТНОЙ шкале");
        barChart2.setCategoryGap(10);
        barChart2.setBarGap(0);

        BarChart<String,Number> barChart3 = new BarChart<>(x3Axis,y3Axis);
        barChart3.setTitle("Веса объектов (нормированные), вычесленные по НЕПРЕРЫВНОЙ  шкале");
        barChart3.setCategoryGap(10);
        barChart3.setBarGap(0);

        BarChart<String,Number> barChart4 = new BarChart<>(x4Axis,y4Axis);
        barChart4.setTitle("Обратные ранги объектов, вычесленные по НЕПРЕРЫВНОЙ  шкале");
        barChart4.setCategoryGap(10);
        barChart4.setBarGap(0);

        BarChart<String,Number> barChart5 = new BarChart<>(x5Axis,y5Axis);
        barChart5.setTitle("Корреляция обратных рангов");
        barChart5.setCategoryGap(10);
        barChart5.setBarGap(10);


        XYChart.Series series11 = new XYChart.Series();
        XYChart.Series series12 = new XYChart.Series();
        XYChart.Series series13 = new XYChart.Series();
        XYChart.Series series14 = new XYChart.Series();

        XYChart.Series series21 = new XYChart.Series();
        XYChart.Series series22 = new XYChart.Series();
        XYChart.Series series23 = new XYChart.Series();
        XYChart.Series series24 = new XYChart.Series();

        XYChart.Series series31 = new XYChart.Series();
        XYChart.Series series32 = new XYChart.Series();
        XYChart.Series series33 = new XYChart.Series();
        XYChart.Series series34 = new XYChart.Series();


        XYChart.Series series41 = new XYChart.Series();
        XYChart.Series series42 = new XYChart.Series();
        XYChart.Series series43 = new XYChart.Series();;
        XYChart.Series series44 = new XYChart.Series();

        XYChart.Series series50 = new XYChart.Series();
        XYChart.Series series51 = new XYChart.Series();
        XYChart.Series series52 = new XYChart.Series();
        XYChart.Series series53 = new XYChart.Series();
        XYChart.Series series54 = new XYChart.Series();
        XYChart.Series series55 = new XYChart.Series();
        XYChart.Series series56 = new XYChart.Series();
        XYChart.Series series57 = new XYChart.Series();
        XYChart.Series series58 = new XYChart.Series();
        XYChart.Series series59 = new XYChart.Series();






        cut1.getData().add(new XYChart.Data<>(sp.getSpCalculations().getLeftCuts().get(0), 1));
        cut1.getData().add(new XYChart.Data<>(sp.getSpCalculations().getRightCuts().get(0),1));
        cut2.getData().add(new XYChart.Data<>(sp.getSpCalculations().getLeftCuts().get(1), 2));
        cut2.getData().add(new XYChart.Data<>(sp.getSpCalculations().getRightCuts().get(1), 2));
        cut3.getData().add(new XYChart.Data<>(sp.getSpCalculations().getLeftCuts().get(2), 3));
        cut3.getData().add(new XYChart.Data<>(sp.getSpCalculations().getRightCuts().get(2), 3));
        cut4.getData().add(new XYChart.Data<>(sp.getSpCalculations().getLeftCuts().get(3), 4));
        cut4.getData().add(new XYChart.Data<>(sp.getSpCalculations().getRightCuts().get(3), 4));
        cut5.getData().add(new XYChart.Data<>(sp.getSpCalculations().getLeftCuts().get(4), 5));
        cut5.getData().add(new XYChart.Data<>(sp.getSpCalculations().getRightCuts().get(4), 5));
        cut6.getData().add(new XYChart.Data<>(sp.getSpCalculations().getLeftCuts().get(5), 6));
        cut6.getData().add(new XYChart.Data<>(sp.getSpCalculations().getRightCuts().get(5), 6));
        cut7.getData().add(new XYChart.Data<>(sp.getSpCalculations().getLeftCuts().get(6), 7));
        cut7.getData().add(new XYChart.Data<>(sp.getSpCalculations().getRightCuts().get(6), 7));

        double [] tmp;
        {
            tmp = sp.getSpCalculations().getFactH();
            series11.getData().add(new XYChart.Data("Fact1", tmp[0]));
            series11.getData().add(new XYChart.Data("Fact2", tmp[1]));
            series11.getData().add(new XYChart.Data("Fact3", tmp[2]));
            series11.getData().add(new XYChart.Data("Fact4", tmp[3]));
            series11.getData().add(new XYChart.Data("Fact5", tmp[4]));
            series11.getData().add(new XYChart.Data("Fact6", tmp[5]));
            series11.getData().add(new XYChart.Data("Fact7", tmp[6]));
            tmp = sp.getSpCalculations().getSumHA();
            series12.getData().add(new XYChart.Data("Sum1", tmp[0]));
            series12.getData().add(new XYChart.Data("Sum2", tmp[1]));
            series12.getData().add(new XYChart.Data("Sum3", tmp[2]));
            series12.getData().add(new XYChart.Data("Sum4", tmp[3]));
            series12.getData().add(new XYChart.Data("Sum5", tmp[4]));
            series12.getData().add(new XYChart.Data("Sum6", tmp[5]));
            series12.getData().add(new XYChart.Data("Sum7", tmp[6]));
            tmp = sp.getSpCalculations().getProizHA();
            series13.getData().add(new XYChart.Data("Proiz1", tmp[0]));
            series13.getData().add(new XYChart.Data("Proiz2", tmp[1]));
            series13.getData().add(new XYChart.Data("Proiz3", tmp[2]));
            series13.getData().add(new XYChart.Data("Proiz4", tmp[3]));
            series13.getData().add(new XYChart.Data("Proiz5", tmp[4]));
            series13.getData().add(new XYChart.Data("Proiz6", tmp[5]));
            series13.getData().add(new XYChart.Data("Proiz7", tmp[6]));
            tmp = sp.getSpCalculations().getLuisHA();
            series14.getData().add(new XYChart.Data("Luis1", tmp[0]));
            series14.getData().add(new XYChart.Data("Luis2", tmp[1]));
            series14.getData().add(new XYChart.Data("Luis3", tmp[2]));
            series14.getData().add(new XYChart.Data("Luis4", tmp[3]));
            series14.getData().add(new XYChart.Data("Luis5", tmp[4]));
            series14.getData().add(new XYChart.Data("Luis6", tmp[5]));
            series14.getData().add(new XYChart.Data("Luis7", tmp[6]));
        }

        {
            tmp = sp.getSpCalculations().getRanksF();
            series21.getData().add(new XYChart.Data("Fact1", tmp[0]));
            series21.getData().add(new XYChart.Data("Fact2", tmp[1]));
            series21.getData().add(new XYChart.Data("Fact3", tmp[2]));
            series21.getData().add(new XYChart.Data("Fact4", tmp[3]));
            series21.getData().add(new XYChart.Data("Fact5", tmp[4]));
            series21.getData().add(new XYChart.Data("Fact6", tmp[5]));
            series21.getData().add(new XYChart.Data("Fact7", tmp[6]));
            tmp = sp.getSpCalculations().getRanksSHA();
            series22.getData().add(new XYChart.Data("Sum1", tmp[0]));
            series22.getData().add(new XYChart.Data("Sum2", tmp[1]));
            series22.getData().add(new XYChart.Data("Sum3", tmp[2]));
            series22.getData().add(new XYChart.Data("Sum4", tmp[3]));
            series22.getData().add(new XYChart.Data("Sum5", tmp[4]));
            series22.getData().add(new XYChart.Data("Sum6", tmp[5]));
            series22.getData().add(new XYChart.Data("Sum7", tmp[6]));
            tmp = sp.getSpCalculations().getRanksPHA();
            series23.getData().add(new XYChart.Data("Proiz1", tmp[0]));
            series23.getData().add(new XYChart.Data("Proiz2", tmp[1]));
            series23.getData().add(new XYChart.Data("Proiz3", tmp[2]));
            series23.getData().add(new XYChart.Data("Proiz4", tmp[3]));
            series23.getData().add(new XYChart.Data("Proiz5", tmp[4]));
            series23.getData().add(new XYChart.Data("Proiz6", tmp[5]));
            series23.getData().add(new XYChart.Data("Proiz7", tmp[6]));
            tmp = sp.getSpCalculations().getRanksLHA();
            series24.getData().add(new XYChart.Data("Lois1", tmp[0]));
            series24.getData().add(new XYChart.Data("Lois2", tmp[1]));
            series24.getData().add(new XYChart.Data("Lois3", tmp[2]));
            series24.getData().add(new XYChart.Data("Lois4", tmp[3]));
            series24.getData().add(new XYChart.Data("Lois5", tmp[4]));
            series24.getData().add(new XYChart.Data("Lois6", tmp[5]));
            series24.getData().add(new XYChart.Data("Lois7", tmp[6]));

        }

        {
            tmp = sp.getSpCalculations().getFactH();
            series31.getData().add(new XYChart.Data("Fact1", tmp[0]));
            series31.getData().add(new XYChart.Data("Fact2", tmp[1]));
            series31.getData().add(new XYChart.Data("Fact3", tmp[2]));
            series31.getData().add(new XYChart.Data("Fact4", tmp[3]));
            series31.getData().add(new XYChart.Data("Fact5", tmp[4]));
            series31.getData().add(new XYChart.Data("Fact6", tmp[5]));
            series31.getData().add(new XYChart.Data("Fact7", tmp[6]));
            tmp = sp.getSpCalculations().getSumHB();
            series32.getData().add(new XYChart.Data("Sum1", tmp[0]));
            series32.getData().add(new XYChart.Data("Sum2", tmp[1]));
            series32.getData().add(new XYChart.Data("Sum3", tmp[2]));
            series32.getData().add(new XYChart.Data("Sum4", tmp[3]));
            series32.getData().add(new XYChart.Data("Sum5", tmp[4]));
            series32.getData().add(new XYChart.Data("Sum6", tmp[5]));
            series32.getData().add(new XYChart.Data("Sum7", tmp[6]));
            tmp = sp.getSpCalculations().getProizHB();
            series33.getData().add(new XYChart.Data("Proiz1", tmp[0]));
            series33.getData().add(new XYChart.Data("Proiz2", tmp[1]));
            series33.getData().add(new XYChart.Data("Proiz3", tmp[2]));
            series33.getData().add(new XYChart.Data("Proiz4", tmp[3]));
            series33.getData().add(new XYChart.Data("Proiz5", tmp[4]));
            series33.getData().add(new XYChart.Data("Proiz6", tmp[5]));
            series33.getData().add(new XYChart.Data("Proiz7", tmp[6]));
            tmp = sp.getSpCalculations().getLuisHB();
            series34.getData().add(new XYChart.Data("Luis1", tmp[0]));
            series34.getData().add(new XYChart.Data("Luis2", tmp[1]));
            series34.getData().add(new XYChart.Data("Luis3", tmp[2]));
            series34.getData().add(new XYChart.Data("Luis4", tmp[3]));
            series34.getData().add(new XYChart.Data("Luis5", tmp[4]));
            series34.getData().add(new XYChart.Data("Luis6", tmp[5]));
            series34.getData().add(new XYChart.Data("Luis7", tmp[6]));
        }

        {
            tmp = sp.getSpCalculations().getRanksF();
            series41.getData().add(new XYChart.Data("Fact1", tmp[0]));
            series41.getData().add(new XYChart.Data("Fact2", tmp[1]));
            series41.getData().add(new XYChart.Data("Fact3", tmp[2]));
            series41.getData().add(new XYChart.Data("Fact4", tmp[3]));
            series41.getData().add(new XYChart.Data("Fact5", tmp[4]));
            series41.getData().add(new XYChart.Data("Fact6", tmp[5]));
            series41.getData().add(new XYChart.Data("Fact7", tmp[6]));
            tmp = sp.getSpCalculations().getRanksSHB();
            series42.getData().add(new XYChart.Data("Sum1", tmp[0]));
            series42.getData().add(new XYChart.Data("Sum2", tmp[1]));
            series42.getData().add(new XYChart.Data("Sum3", tmp[2]));
            series42.getData().add(new XYChart.Data("Sum4", tmp[3]));
            series42.getData().add(new XYChart.Data("Sum5", tmp[4]));
            series42.getData().add(new XYChart.Data("Sum6", tmp[5]));
            series42.getData().add(new XYChart.Data("Sum7", tmp[6]));
            tmp = sp.getSpCalculations().getRanksPHB();
            series43.getData().add(new XYChart.Data("Proiz1", tmp[0]));
            series43.getData().add(new XYChart.Data("Proiz2", tmp[1]));
            series43.getData().add(new XYChart.Data("Proiz3", tmp[2]));
            series43.getData().add(new XYChart.Data("Proiz4", tmp[3]));
            series43.getData().add(new XYChart.Data("Proiz5", tmp[4]));
            series43.getData().add(new XYChart.Data("Proiz6", tmp[5]));
            series43.getData().add(new XYChart.Data("Proiz7", tmp[6]));
            tmp = sp.getSpCalculations().getRanksLHB();
            series44.getData().add(new XYChart.Data("Lois1", tmp[0]));
            series44.getData().add(new XYChart.Data("Lois2", tmp[1]));
            series44.getData().add(new XYChart.Data("Lois3", tmp[2]));
            series44.getData().add(new XYChart.Data("Lois4", tmp[3]));
            series44.getData().add(new XYChart.Data("Lois5", tmp[4]));
            series44.getData().add(new XYChart.Data("Lois6", tmp[5]));
            series44.getData().add(new XYChart.Data("Lois7", tmp[6]));

        }
        tmp = sp.getSpCalculations().getCorrellArray();

        series50.getData().add(new XYChart.Data("RanksF", tmp[0]));
        series51.getData().add(new XYChart.Data("RanksSHA", tmp[1]));
        series52.getData().add(new XYChart.Data("RanksPHA", tmp[2]));
        series53.getData().add(new XYChart.Data("RanksLHA", tmp[3]));
        series54.getData().add(new XYChart.Data("RanksAvgA", tmp[4]));
        series55.getData().add(new XYChart.Data("RanksSHB", tmp[5]));
        series56.getData().add(new XYChart.Data("RanksPHB", tmp[6]));
        series57.getData().add(new XYChart.Data("RanksLHB", tmp[7]));
        series58.getData().add(new XYChart.Data("RanksAvgB", tmp[8]));
        series59.getData().add(new XYChart.Data("RanksAvg", tmp[9]));

        lineChart0.getData().add(cut1);
        lineChart0.getData().add(cut2);
        lineChart0.getData().add(cut3);
        lineChart0.getData().add(cut4);
        lineChart0.getData().add(cut5);
        lineChart0.getData().add(cut6);
        lineChart0.getData().add(cut7);

        barChart1.getData().add(series11);
        barChart1.getData().addAll(series12);
        barChart1.getData().addAll(series13);
        barChart1.getData().addAll(series14);

        barChart2.getData().addAll(series21);
        barChart2.getData().addAll(series22);
        barChart2.getData().addAll(series23);
        barChart2.getData().addAll(series24);

        barChart3.getData().addAll(series31);
        barChart3.getData().addAll(series32);
        barChart3.getData().addAll(series33);
        barChart3.getData().addAll(series34);

        barChart4.getData().addAll(series41);
        barChart4.getData().addAll(series42);
        barChart4.getData().addAll(series43);
        barChart4.getData().addAll(series44);

        barChart5.getData().addAll(series50);
        barChart5.getData().addAll(series51);
        barChart5.getData().addAll(series52);
        barChart5.getData().addAll(series53);
        barChart5.getData().addAll(series54);
        barChart5.getData().addAll(series55);
        barChart5.getData().addAll(series56);
        barChart5.getData().addAll(series57);
        barChart5.getData().addAll(series58);
        barChart5.getData().addAll(series59);



        GridPane pane = new GridPane();
        pane.add(lineChart0, 0, 0);
        pane.add(barChart1,1,0);
        pane.add(barChart2,2,0);
        pane.add(barChart3,0,1);
        pane.add(barChart4,1,1);
        pane.add(barChart5,2,1);

        Scene scene = new Scene(pane, 1920, 1000);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        //FirstPage fp = new FirstPage();
        //launch();
        AnalisysOfModeling analisysOfModeling = new AnalisysOfModeling();


    }
}