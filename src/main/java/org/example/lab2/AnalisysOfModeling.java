package org.example.lab2;

import java.util.ArrayList;

public class AnalisysOfModeling {
    ArrayList<ArrayList> hugeTab;
    ArrayList<Double> Arr1, Arr2, Arr3, Arr4, Arr5, Arr6, Arr7, Arr8, Arr9;

    double [][] MatrixCorrel;

    public AnalisysOfModeling(){
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
        for (int i = 0 ;i < 100 ;++i) {
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
        selectParametres();
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
    }

    public void printCorrelMatrix(){
        for (int i = 0; i < hugeTab.size(); ++i) {
            for (int j = 0; j < hugeTab.size(); ++j) {
                System.out.print(MatrixCorrel[i][j] + " ");
            }
            System.out.print("\n");
        }
    }

    public void selectParametres(){
        int indA9 = -1 , indA8 = -1, indA7 = -1, indB9 = -1, indB8 = -1, indB7 = -1;
        double valA9 = -1 , valA8 = -1, valA7 = -1, valB9 = -1, valB8 = -1, valB7 = -1, min = 0, max = 0;
        for (int i = 0; i < MatrixCorrel.length; ++i) {
            for (int j = 0; j < MatrixCorrel.length; ++j) {
                if ((Math.abs(MatrixCorrel[i][j]) >= 0.9) && (Math.abs(MatrixCorrel[i][j]) < 1.0)) {
                    if (MatrixCorrel[i][j] > 0) {
                        if (MatrixCorrel[i][j] > max) {
                            max = MatrixCorrel[i][j];
                            indA9 = i;
                        }
                    } else if (MatrixCorrel[i][j] < 0)
                        if (Math.abs(MatrixCorrel[i][j]) > Math.abs(min)) {
                            min = MatrixCorrel[i][j];
                            indB9 = j;
                        }
                }
            }
        }//9
        valA9 = max;
        valB9 = min;
        System.out.println(indA9 + " : " + valA9 + "; " + indB9 + " : " + valB9);
    }
}
