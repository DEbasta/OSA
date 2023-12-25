package com.example.lab2;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class AnalisysOfModeling {
    final int genHugeAmount = 100, genFinAmount = 30;
    boolean bordersReady = true;
    ArrayList<ArrayList> hugeTab, finTab;
    ArrayList<Double> Arr1, Arr2, Arr3, Arr4, Arr5, Arr6, Arr7, Arr8, Arr9, Borders, Fin1, Fin2, Fin3, Fin4, Fin5,
            Fin6, Fin7, Fin8, Fin9, Fin10, Fin11;

    double [][] MatrixCorrel;

    public AnalisysOfModeling(){
        genHugeTab();
        genSecondTab();
    }

    public void genHugeTab(){
        hugeTab = new ArrayList<>();
        Arr1 = new ArrayList<>();
        Arr2 = new ArrayList<>();
        Arr3 = new ArrayList<>();
        Arr4 = new ArrayList<>();
        Arr5 = new ArrayList<>();
        Arr6 = new ArrayList<>();
        Arr7 = new ArrayList<>();
        Arr8 = new ArrayList<>();
        Arr9 = new ArrayList<>();

        hugeTab.add(Arr1);
        hugeTab.add(Arr2);
        hugeTab.add(Arr3);
        hugeTab.add(Arr4);
        hugeTab.add(Arr5);
        hugeTab.add(Arr6);
        hugeTab.add(Arr7);
        hugeTab.add(Arr8);
        hugeTab.add(Arr9);
        SMOin smoIn = new SMOin();
        for (int i = 0; i < genHugeAmount; ++i) {
            SMO smo = new SMO(smoIn);
            Arr1.add(smo.ClockTime);
            Arr2.add(smo.AvgTimeInQue);
            Arr3.add(smo.MaxTimeInQu);
            Arr4.add(smo.AvgNumInQue);
            Arr5.add(Double.valueOf(smo.MaxNumInQu));
            Arr6.add(smo.AvgServersBusyPercent);
            Arr7.add(Double.valueOf(smo.NumServed));
            Arr8.add(Double.valueOf(smo.NumLost));
            Arr9.add(smo.NumLostPercent);
        }
        MatrixCorrel = new double[hugeTab.size()][hugeTab.size()];
        fillCorrelMatrix();
    }

    public void printHugeTab(){
        System.out.println();
        for (int i = 0; i < genHugeAmount; ++i) {
            System.out.println(hugeTab.get(0).get(i) + " " + hugeTab.get(1).get(i) + " " + hugeTab.get(2).get(i) + " " +
                    hugeTab.get(3).get(i) + " " + hugeTab.get(4).get(i) + " " + hugeTab.get(5).get(i) + " " +
                    hugeTab.get(6).get(i) + " " + hugeTab.get(7).get(i) + " " + hugeTab.get(8).get(i));
        }
        System.out.println();
    }

    public double coefCorrel(ArrayList<Double> arr1, ArrayList<Double> arr2) {
        double xSred = 0, ySred = 0, chisl = 0, znamen, z1 = 0, z2 = 0, coefCorrel;
        for (int i = 0; i < arr1.size(); ++i) {
            xSred += arr1.get(i);
            ySred += arr2.get(i);
        }
        xSred/=arr1.size();
        ySred/=arr1.size();
        for (int i = 0; i < arr1.size(); ++i) {
            chisl += ((arr1.get(i)-xSred)*(arr2.get(i)-ySred));
            z1 += Math.pow((arr1.get(i)-xSred),2);
            z2 += Math.pow((arr2.get(i)-ySred),2);
        }
        znamen = Math.sqrt(z1*z2);
        coefCorrel = chisl/znamen;
        return coefCorrel;
    }

    public void fillCorrelMatrix(){
        int step = 0;
        for (int i = 0; i < hugeTab.size(); ++i) {
            for (int j = 0; j < hugeTab.size();++j) {
                if (i == j)
                    MatrixCorrel[j][j] = 1.0;
                if (j == step)
                    j = hugeTab.size();
                else MatrixCorrel[i][j] = coefCorrel(hugeTab.get(i), hugeTab.get(j));
            }
            ++step;
        }
        printCorrelMatrix();
        getBorders();
        System.out.println("---------------------------------------------------------------------------------------------");
        System.out.println("Итоговые параметры");
        System.out.println(Borders.get(0) + " : " + Borders.get(1) + " " + Borders.get(2) + " : " + Borders.get(3));
        System.out.println("---------------------------------------------------------------------------------------------");
    }

    public void printCorrelMatrix(){
        System.out.println("Матрица коэфицентов корреляции");
        System.out.println("---------------------------------------------------------------------------------------------");
        for (int i = 0; i < hugeTab.size(); ++i) {
            for (int j = 0; j < hugeTab.size(); ++j) {
                System.out.print(MatrixCorrel[i][j] + " ");
            }
            System.out.print("\n");
        }
    }

    public ArrayList<Double> selectParametres(double level){
        int indA = -1 , indB = -1;
        double valA = -1 , valB = -1, min = 0, max = 0;
        for (int i = 0; i < MatrixCorrel.length; ++i) {
            for (int j = 0; j < MatrixCorrel.length; ++j) {
                if ((Math.abs(MatrixCorrel[i][j]) >= level) && (Math.abs(MatrixCorrel[i][j]) < (level+0.1))) {
                    if (MatrixCorrel[i][j] > 0) {
                        if (MatrixCorrel[i][j] > max) {
                            max = MatrixCorrel[i][j];
                            indA = i+1;
                        }
                    } else if (MatrixCorrel[i][j] < 0)
                        if (Math.abs(MatrixCorrel[i][j]) > Math.abs(min)) {//всё правильно
                            min = MatrixCorrel[i][j];
                            indB = j+1;
                        }
                }
            }
        }
        valA = max;
        valB = min;
        min = 0;
        max = 1;


        if (indA == -1){
            for (int i = 0; i < MatrixCorrel.length; ++i) {
                for (int j = 0; j < MatrixCorrel.length; ++j) {
                    if ((Math.abs(MatrixCorrel[i][j]) >= level) && (Math.abs(MatrixCorrel[i][j]) < (level+0.1))) {
                        if (MatrixCorrel[i][j] < 0)
                            if (Math.abs(MatrixCorrel[i][j]) > Math.abs(min)&&(valB < MatrixCorrel[i][j])) {
                                min = MatrixCorrel[i][j];
                                indA = i+1;
                            }
                    }
                }
            }
            valA = min;
        }

        boolean flag = false;
        if (indB == -1){
            for (int i = 0; i < MatrixCorrel.length; ++i) {
                for (int j = 0; j < MatrixCorrel.length; ++j) {
                    if ((MatrixCorrel[i][j] >= level) && (MatrixCorrel[i][j] < (level+0.1))) {
                        if (MatrixCorrel[i][j] > 0)
                            if ((MatrixCorrel[i][j] < max) && (valA > MatrixCorrel[i][j])) {
                                max = MatrixCorrel[i][j];
                                indB = j+1;
                                flag = true;
                            }
                    }
                }
            }
            if (flag)
                valB = max;
        }
        System.out.println("---------------------------------------------------------------------------------------------");
        System.out.println("Выбор выходных показателей для уровня : " + level);
        System.out.println(indA + " : " + valA + "; " + indB + " : " + valB);
        ArrayList<Double> retList = new ArrayList<>();
        retList.add((double)indA);
        retList.add(valA);
        retList.add((double)indB);
        retList.add(valB);
        return retList;
    }

    public void getBorders() {
        ArrayList<Double> retList = new ArrayList<>(), tmp1, tmp2, tmp3;
        double min = 0, max = 0;
        double minInd = -1, maxInd = -1;
        tmp1 = selectParametres(0.9);
        tmp2 = selectParametres(0.8);
        tmp3 = selectParametres(0.7);

        for (int i = 0; i <= 2; i+=2) {

            if (tmp1.get(i) != -1) {
                retList.add(tmp1.get(i));
                retList.add(tmp1.get(i+1));
            }

            if (tmp2.get(i) != -1) {
                retList.add(tmp2.get(i));
                retList.add(tmp2.get(i+1));
            }

            if (tmp3.get(i) != -1) {
                retList.add(tmp3.get(i));
                retList.add(tmp3.get(i+1));
            }
        }

        if (retList.size() < 4){
            genHugeTab();
        }
        try {
            max = retList.get(1);
            min = retList.get(3);
        }
        catch (IndexOutOfBoundsException e){}

        for (int i = 1 ; i < retList.size(); i+=2) {
            if (retList.get(i) >= max){
                max = retList.get(i);
                maxInd = retList.get(i - 1);
            }
            if ((retList.get(i) <= min) && (Math.round(maxInd) != Math.round(retList.get(i-1)))){
                min = retList.get(i);
                minInd = retList.get(i - 1);
            }
        }

        if (Math.round(maxInd) == Math.round(minInd)) {
            genHugeTab();
        }
        retList = new ArrayList<>();
        retList.add(maxInd);
        retList.add(max);
        retList.add(minInd);
        retList.add(min);
//        System.out.println("fin " + maxInd + " : " + max + " " + minInd + " : " + min);
        if (Borders == null)
            Borders = retList;
    }

    public void genSecondTab() {
        if (Borders != null) {
            Fin1 = new ArrayList<>();
            Fin2 = new ArrayList<>();
            Fin3 = new ArrayList<>();
            Fin4 = new ArrayList<>();
            Fin5 = new ArrayList<>();
            Fin6 = new ArrayList<>();
            Fin7 = new ArrayList<>();
            Fin8 = new ArrayList<>();
            Fin9 = new ArrayList<>();
            Fin10 = new ArrayList<>();
            Fin11 = new ArrayList<>();

            finTab = new ArrayList<>();
            finTab.add(Fin1);
            finTab.add(Fin2);
            finTab.add(Fin3);
            finTab.add(Fin4);
            finTab.add(Fin5);
            finTab.add(Fin6);
            finTab.add(Fin7);
            finTab.add(Fin8);
            finTab.add(Fin9);
            finTab.add(Fin10);
            finTab.add(Fin11);
            for (int i = 0; i < genFinAmount; ++i) {
                SMOin smoIn = new SMOin();
                SMO smo = new SMO(smoIn);
                Fin1.add(smoIn.getMeanIATime());
                Fin2.add(smoIn.getMeanServeTime());
                Fin3.add(Double.valueOf(smoIn.getNumServers()));
                Fin4.add(Math.pow(smoIn.getMeanIATime(), 2));
                Fin5.add(smoIn.getMeanIATime() * smoIn.getMeanServeTime());
                Fin6.add(smoIn.getMeanIATime() * Double.valueOf(smoIn.getNumServers()));
                Fin7.add(Math.pow(smoIn.getMeanServeTime(), 2));
                Fin8.add(smoIn.getMeanServeTime() * Double.valueOf(smoIn.getNumServers()));
                Fin9.add(Math.pow(Double.valueOf(smoIn.getNumServers()), 2));
                Fin10.add(getParam(smo, (int)Math.round(Borders.get(0))));//y
                Fin11.add(getParam(smo, (int)Math.round(Borders.get(2))));
            }
            try {
                saveValues();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void printSecondTab() {
        for (int i = 0; i < genFinAmount; ++i) {
            System.out.println(finTab.get(0).get(i) + " " + finTab.get(1).get(i) + " " + finTab.get(2).get(i) + " " +
                    finTab.get(3).get(i) + " " + finTab.get(4).get(i) + " " + finTab.get(5).get(i) + " " +
                    finTab.get(6).get(i) + " " + finTab.get(7).get(i) + " " + finTab.get(8).get(i) + " " +
                    finTab.get(9).get(i) + " " + finTab.get(10).get(i));
        }
    }

    public double getParam(SMO smo, int ind){
        double retStatment = 0.0;
        switch (ind){
            case (1):{
                retStatment = smo.getClockTime();
                break;
            }
            case (2):{
                retStatment = smo.getAvgTimeInQue();
                break;
            }
            case (3):{
                retStatment = smo.getMaxTimeInQu();
                break;
            }
            case (4):{
                retStatment = smo.getAvgNumInQue();
                break;
            }
            case (5):{
                retStatment = Double.valueOf(smo.getMaxNumInQu());
                break;
            }
            case (6):{
                retStatment = smo.getAvgServersBusyPercent();
                break;
            }
            case (7):{
                retStatment = Double.valueOf(smo.getNumServed());
                break;
            }
            case (8):{
                retStatment = Double.valueOf(smo.getNumLost());
                break;
            }
            case (9):{
                retStatment = smo.getNumLostPercent();
                break;
            }

        }
        return retStatment;
    }

    public void saveValues() throws IOException {
        FileWriter x1 = new FileWriter("x1.txt");
        FileWriter x2 = new FileWriter("x2.txt");
        FileWriter x3 = new FileWriter("x3.txt");
        FileWriter x4 = new FileWriter("x4.txt");
        FileWriter x5 = new FileWriter("x5.txt");
        FileWriter x6 = new FileWriter("x6.txt");
        FileWriter x7 = new FileWriter("x7.txt");
        FileWriter x8 = new FileWriter("x8.txt");
        FileWriter x9 = new FileWriter("x9.txt");
        FileWriter y1 = new FileWriter("y1.txt");
        FileWriter y2 = new FileWriter("y2.txt");


        for (int i = 0; i < genFinAmount; ++i) {
            x1.write(finTab.get(0).get(i)+" ");
            x2.write(finTab.get(1).get(i)+" ");
            x3.write(finTab.get(2).get(i)+" ");
            x4.write(finTab.get(3).get(i)+" ");
            x5.write(finTab.get(4).get(i)+" ");
            x6.write(finTab.get(5).get(i)+" ");
            x7.write(finTab.get(6).get(i)+" ");
            x8.write(finTab.get(7).get(i)+" ");
            x9.write(finTab.get(8).get(i)+" ");
            y1.write(finTab.get(9).get(i)+" ");
            y2.write(finTab.get(10).get(i)+" ");
        }
        x1.flush();
        x2.flush();
        x3.flush();
        x4.flush();
        x5.flush();
        x6.flush();
        x7.flush();
        x8.flush();
        x9.flush();
        y1.flush();
        y2.flush();

        x1.close();
        x2.close();
        x3.close();
        x4.close();
        x5.close();
        x6.close();
        x7.close();
        x8.close();
        x9.close();
        y1.close();
        y2.close();
    }


    //=1 - (1 - C244)*((C247-1)/(C247-4))
}
