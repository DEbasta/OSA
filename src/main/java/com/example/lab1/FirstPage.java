package com.example.lab1;

public class FirstPage {
    FPCalculations fpCalculations;
    public FirstPage(){
        int N = 7;
        double AZad [] = {3,5,4,6,4,2,1};
        double AFirst [] = {4,2,4,1,4,6,7};
        fpCalculations = new FPCalculations(N, AZad, AFirst);
    }
}
