package org.example.lab1;

public class FirstPage {
    FPCalculations fpCalculations;
    public FirstPage(){
        int N = 7;
        double AZad [] = {5,2,4,1,4,6,7};
        double AFirst [] = {4,2,4,1,4,6,7};
        fpCalculations = new FPCalculations(N, AZad, AFirst);
    }
}
