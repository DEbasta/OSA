package org.example.lab2;

import java.util.ArrayList;

public class AnalisysOfModeling {
    final int genAmount = 100;
    ArrayList<ArrayList> hugeTab;
    ArrayList<Double> Arr1, Arr2, Arr3, Arr4, Arr5, Arr6, Arr7, Arr8, Arr9, Borders;

    double [][] MatrixCorrel;

    public AnalisysOfModeling(){
        genHugeTab();
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
        for (int i = 0 ;i < genAmount ;++i) {
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
        for (int i = 0 ; i < genAmount; ++i) {
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
        Borders = getBorders();
    }

    public void printCorrelMatrix(){
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

        System.out.println(indA + " : " + valA + "; " + indB + " : " + valB);
        ArrayList<Double> retList = new ArrayList<>();
        retList.add((double)indA);
        retList.add(valA);
        retList.add((double)indB);
        retList.add(valB);
        return retList;
    }

    public ArrayList<Double> getBorders() {
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
        System.out.println("fin " + maxInd + " : " + max + " " + minInd + " : " + min);
        return retList;
    }
}
