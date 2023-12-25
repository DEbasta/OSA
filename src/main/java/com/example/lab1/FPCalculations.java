package com.example.lab1;

import java.util.Arrays;

public class FPCalculations {
    int N;
    double AZad [];
    double AFirst [];
    double AMatrix [][];
    double Weights [];
    double Ranks [];
    double CoefCorrel;

    public FPCalculations(int N, double[] AZad, double[] AFirst) {
        this.AZad = AZad;
        this.AFirst = AFirst;
        this.N = N;
        AMatrix = new double[N][N];
        setMatrix();
        countWeights();
        countRanks();
        coefCorrel();
        printSheet();
    }

    public void printSheet(){
        System.out.println("First page Info");
        System.out.println("---------------------------------------------------------------------------------------------");
        System.out.println("Matrix");
        for (int i = 0; i < N; ++i){
            for (int j = 0; j < N; ++j)
                System.out.print(AMatrix[i][j] + " ");
            System.out.println();
        }
        System.out.println();
        System.out.println("Weights | Ranks");
        for ( int i = 0; i < N; ++i) {
            System.out.println(Weights[i] + " " + Ranks[i]);
        }
        System.out.println();
        System.out.println("Ranks 1 | Ranks 2");
        for ( int i = 0; i < N; ++i) {
            System.out.println(AZad[i] + " " + Ranks[i]);
        }
        System.out.println();
        System.out.println("Coeficeint of Correlation");
        System.out.println(-CoefCorrel);
        System.out.println("---------------------------------------------------------------------------------------------");
    }

    private void setMatrix(){
        for (int i = 0; i < N; ++i)
            for (int j = i; j < N;++j){
                if (AZad[i] == AZad[j])
                    AMatrix[i][j] = 2;
                else if (AZad[i] > AZad[j])
                    AMatrix[i][j] = 1;
                else
                    AMatrix[i][j] = 3;
            }
        for (int i = N-1; i >= 0; --i)
            for (int j = i; j >=0 ;--j){
                if (AZad[i] == AZad[j])
                    AMatrix[i][j] = 2;
                else if (AZad[i] > AZad[j])
                    AMatrix[i][j] = 1;
                else
                    AMatrix[i][j] = 3;
            }

    }

    public void countWeights(){
        double Sum = 0.0;
        Weights  = new double[N];
        for (int i = 0; i < N; ++i)
            for (int j = 0; j < N; ++j)
                Sum += AMatrix[i][j];
        for (int i = 0; i < N; ++i){
            double tmpSum = 0.0;
            for (int j = 0; j < N; ++j)
                tmpSum+=AMatrix[i][j];
            Weights[i] = tmpSum/Sum;
        }
    }

    public void countRanks(){
        double copyW [] = new double[N], tmpRanks [] = new double[N];
        Ranks = new double[N];
        int rankCounter = N;
        double sum = 0;
        System.arraycopy(Weights, 0, copyW,0, N);
        copyW = Arrays.stream(copyW).sorted().toArray();
        for ( int i = 0; i < N; ++i){//распределяю ранги
            tmpRanks[i] = rankCounter;
            --rankCounter;
        }
        rankCounter = 0; //определяю одинаковые ранги
        for ( int i = 1; i < N; ++i){
            if (copyW[i] == copyW[i-1]) {
                ++rankCounter;
                sum += tmpRanks[i-1];
            } else if ((rankCounter != 0)) {
                ++rankCounter;
                sum += tmpRanks[i-1];
                sum = sum / rankCounter;
                for (int j = rankCounter; j > 0; --j) {
                    tmpRanks[i-j] = sum;
                }
                rankCounter = 0;
                sum = 0;
            }
        }
        for ( int i = 0; i < N; ++i){
            for (int j = 0 ; j < N; ++j){
                if (Weights[i] == copyW[j])
                    Ranks[i] = tmpRanks[j];
            }
        }
    }

    public void coefCorrel(){ //формула для коэфицента отсюда https://correlation-denis.narod.ru/correl.htm
        double xSred = 0, ySred = 0, chisl = 0, znamen = 0, z1 = 0, z2 = 0;
        for (int i = 0; i < N; ++i) {
            xSred += AFirst[i];
            ySred += Ranks[i];
        }
        xSred/=N;
        ySred/=N;
        for (int i = 0; i < N; ++i) {
            chisl += ((AFirst[i]-xSred)*(Ranks[i]-ySred));
            z1 += Math.pow((AFirst[i]-xSred),2);
            z2 += Math.pow((Ranks[i]-ySred),2);
        }
        znamen = Math.sqrt(z1*z2);
        CoefCorrel = chisl/znamen;
    }



}
