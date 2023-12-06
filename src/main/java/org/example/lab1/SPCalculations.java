package org.example.lab1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class SPCalculations {
    int N = 7;
    ArrayList<Double> Cuts = new ArrayList<>(), LeftCuts = new ArrayList<>(), RightCuts = new ArrayList<>();
    Random random = new Random();
    double [][] AMatrix, AMatrixLuis, BMatrix, BMatrixLuis;
    double [] SumHA = new double[N], ProizHA = new double[N], LuisHA = new double[N], SumHB = new double[N], ProizHB = new double[N], LuisHB = new double[N],  tmpSum1 = new double[N], tmpSum2 = new double[N], tmpSum = new double[N];
    double [] RanksSHA,RanksPHA, RanksLHA, RanksSHB,RanksPHB, RanksLHB, RanksF, RanksAvgA, RanksAvgB, RanksAvg;
    public SPCalculations(){
        double cutsSum = 0;
        for (int i =0; i < N; ++i) {
            Cuts.add(7 + random.nextInt(3) + random.nextDouble());
            cutsSum += Cuts.get(i);
            LeftCuts.add(random.nextInt(11) + random.nextDouble());
            RightCuts.add(Cuts.get(i) + LeftCuts.get(i));
        }

        setMatrixA();
        sumMethod(SumHA,AMatrix);
        RanksSHA = countRanks(SumHA);
        proizMethod(ProizHA, AMatrix);
        RanksPHA = countRanks(ProizHA);
        AMatrixLuis = new double[N][N];
        setMatrixLuis( AMatrix, AMatrixLuis);
        luisMethod(LuisHA, AMatrixLuis);
        RanksLHA = countRanks(LuisHA);

        setMatrixB();
        sumMethod(SumHB,BMatrix);
        RanksSHB = countRanks(SumHB);
        proizMethod(ProizHB, BMatrix);
        RanksPHB = countRanks(ProizHB);
        BMatrixLuis = new double[N][N];
        setMatrixLuis( BMatrix, BMatrixLuis);
        luisMethod(LuisHB, BMatrixLuis);
        RanksLHB = countRanks(LuisHA);


        for (int i = 0; i < N; ++i){
            tmpSum[i] = Cuts.get(i)/cutsSum;
            tmpSum1[i] = RanksSHA[i] + RanksPHA[i] + RanksLHA[i];
            tmpSum2[i] = RanksSHB[i] + RanksPHB[i] + RanksLHB[i];
        }
        RanksF = countRanks(tmpSum);
        RanksAvgA = countRanks(tmpSum1);
        RanksAvgB = countRanks(tmpSum2);
        for (int i = 0; i < N; ++i){
            tmpSum[i] = tmpSum1[i] + tmpSum2[i];
        }
        RanksAvg = countRanks(tmpSum);
        printSheet();
    }

    public void printSheet() {
        System.out.println("Cuts");
        for (int i = 0; i < N; ++i) {
            System.out.println(Cuts.get(i));
        }
        System.out.println();
        System.out.println("Matrix A");
        for (int i = 0; i < N; ++i) {
            for (int j = 0; j < N; ++j)
                System.out.print(AMatrix[i][j] + " ");
            System.out.println();
        }
        System.out.println();
        System.out.println("met Sloj A");
        System.out.println("SumH  |  RankSHA");
        for (int i = 0; i < N; ++i) {
            System.out.println(SumHA[i] + "     " + RanksSHA[i]);
        }
        System.out.println();
        System.out.println("met Proizved A");
        System.out.println("ProizH  |  RankPHA");
        for (int i = 0; i < N; ++i) {
            System.out.println(ProizHA[i] + "     " + RanksPHA[i]);
        }
        System.out.println();
        System.out.println("Matrix Luis A");
        for (int i = 0; i < N; ++i) {
            for (int j = 0; j < N; ++j)
                System.out.print(AMatrixLuis[i][j] + " ");
            System.out.println();
        }
        System.out.println();
        System.out.println("met Luisa A");
        System.out.println("LuisH  |  RankLHA");
        for (int i = 0; i < N; ++i) {
            System.out.println(LuisHA[i] + "     " + RanksLHA[i]);
        }//конец первых таблиц
        System.out.println();
        System.out.println("Matrix B");
        for (int i = 0; i < N; ++i) {
            for (int j = 0; j < N; ++j)
                System.out.print(BMatrix[i][j] + " ");
            System.out.println();
        }
        System.out.println();
        System.out.println("met Sloj B");
        System.out.println("SumHB  |  RankSHB");
        for (int i = 0; i < N; ++i) {
            System.out.println(SumHB[i] + "     " + RanksSHB[i]);
        }
        System.out.println();
        System.out.println("met Proizved B");
        System.out.println("ProizHB  |  RankPHB");
        for (int i = 0; i < N; ++i) {
            System.out.println(ProizHB[i] + "     " + RanksPHB[i]);
        }
        System.out.println();
        System.out.println("Matrix Luis B");
        for (int i = 0; i < N; ++i) {
            for (int j = 0; j < N; ++j)
                System.out.print(BMatrixLuis[i][j] + " ");
            System.out.println();
        }
        System.out.println();
        System.out.println("met Luisa B");
        System.out.println("LuisHB  |  RankLHB");
        for (int i = 0; i < N; ++i) {
            System.out.println(LuisHB[i] + "     " + RanksLHB[i]);
        }//конец вторых таблиц

        System.out.println();
        System.out.println("Total Ranks");
        System.out.println("RanksF  |  RanksSHA  |  RanksPHA  |  RanksLHA  |  RanksAvgA  |  RanksSHB  |  RanksPHB  |  RanksLHB  |  RanksAvgB  |  RanksAvg");
        for (int i = 0; i < N; ++i) {
            System.out.println(RanksF[i] + "     |     " + RanksSHA[i] + "    |     " + RanksPHA[i] + "    |     "  + RanksLHA[i] + "    |     " + RanksAvgA[i] + "     |     " + RanksSHB[i] + "    |    " + RanksPHB[i] + "     |    " + RanksLHB[i] + "     |     " + RanksAvgB[i] + "     |    " + RanksAvg[i]);
        }
    }
    private void setMatrixA(){
        AMatrix = new double[N][N];
        for (int i = 0; i < N; ++i)
            for (int j = i; j < N;++j){
                if (Cuts.get(i) == Cuts.get(j))
                    AMatrix[i][j] = 2;
                else if (Cuts.get(i) > Cuts.get(j))
                    AMatrix[i][j] = 3;
                else
                    AMatrix[i][j] = 1;
            }
        for (int i = N-1; i >= 0; --i)
            for (int j = i; j >=0 ;--j){
                if (Cuts.get(i) == Cuts.get(j))
                    AMatrix[i][j] = 2;
                else if (Cuts.get(i) > Cuts.get(j))
                    AMatrix[i][j] = 3;
                else
                    AMatrix[i][j] = 1;
            }
    }
    private void setMatrixLuis(double [][] MatrixA, double [][] MatrixL){
        for (int i = 0; i < N; ++i) {
            for (int j = 0; j < N; ++j){
                MatrixL[i][j] = MatrixA[j][i]/MatrixA[i][j];
            }
        }
    }

    private void setMatrixB() {
        BMatrix = new double[N][N];
        double a;
        for (int i = 0; i < N; ++i) {
            for (int j = i; j < N; ++j) {
                if (Cuts.get(i) == Cuts.get(j))
                    BMatrix[i][j] = 0.5;
                else if (Cuts.get(i) > Cuts.get(j)) {
                    a = Cuts.get(i) - Cuts.get(j);
                    if (a > 2.0)
                        BMatrix[i][j] = 0.9;
                    else if (a > 1.5)
                        BMatrix[i][j] = 0.8;
                    else if (a > 1.0)
                        BMatrix[i][j] = 0.7;
                    else
                        BMatrix[i][j] = 0.6;
                }
                else {
                    a = Math.abs(Cuts.get(i) - Cuts.get(j));
                    if (a > 2.0)
                        BMatrix[i][j] = 0.1;
                    else if (a > 1.5)
                        BMatrix[i][j] = 0.2;
                    else if (a > 1.0)
                        BMatrix[i][j] = 0.3;
                    else
                        BMatrix[i][j] = 0.4;
                }
            }
        }

        for (int i = N-1; i >= 0; --i) {
            for (int j = i; j >= 0; --j) {
                if (Cuts.get(i) == Cuts.get(j))
                    BMatrix[i][j] = 0.5;
                else if (Cuts.get(i) > Cuts.get(j)) {
                    a = Cuts.get(i) - Cuts.get(j);
                    if (a > 2.0)
                        BMatrix[i][j] = 0.9;
                    else if (a > 1.5)
                        BMatrix[i][j] = 0.8;
                    else if (a > 1.0)
                        BMatrix[i][j] = 0.7;
                    else
                        BMatrix[i][j] = 0.6;
                } else {
                    a = Math.abs(Cuts.get(i) - Cuts.get(j));
                    if (a > 2.0)
                        BMatrix[i][j] = 0.1;
                    else if (a > 1.5)
                        BMatrix[i][j] = 0.2;
                    else if (a > 1.0)
                        BMatrix[i][j] = 0.3;
                    else
                        BMatrix[i][j] = 0.4;
                }
            }
        }
    }
    public void sumMethod(double [] SH, double[][] Matrix) {
        double sumAll = 0;
        double [] sum = new double[N];
        for (int i = 0; i < N; ++i) {
            for (int j = 0; j < N; ++j) {
                sum[i] = sum[i] + Matrix[i][j];
            }
        }
        for (int i = 0; i < N; ++i) {
            sumAll += sum[i];
        }
        for (int i = 0; i < N; ++i) {
            SH[i] = sum[i] / sumAll;
        }
    }

    public void proizMethod(double[] PH, double[][] Matrix){
        double sumAll = 0;
        double [] proiz = new double[N];
        Arrays.fill(proiz, 1);
        for (int i = 0; i < N; ++i) {
            for (int j = 0; j < N; ++j) {
                proiz[i] = proiz[i] * Matrix[i][j];
            }
        }
        for (int i = 0; i < N; ++i) {
            sumAll += proiz[i];
        }
        for (int i = 0; i < N; ++i) {
            PH[i] = proiz[i] / sumAll;
        }
    }

    public void luisMethod(double[] LH, double[][] Matrix){
        double sumAll = 0;
        double [] sum = new double[N];
        Arrays.fill(sum, 0);
        for (int i = 0; i < N; ++i) {
            for (int j = 0; j < N; ++j) {
                sum[i] = (sum[i] + Matrix[i][j]);
            }
            
        }
        for (int i = 0; i < N; ++i) {
            sum[i] = 1 / sum[i];
            sumAll += sum[i];
        }
        for (int i = 0; i < N; ++i) {
            LH[i] = (double) sum[i] / sumAll;
        }
    }

    public double[] countRanks(double [] arr){
        double [] copyW  = new double[N], tmpRanks  = new double[N], ranks = new double[N];
        int rankCounter = 1;
        double sum = 0;
        System.arraycopy(arr, 0, copyW,0, arr.length);
        copyW = Arrays.stream(copyW).sorted().toArray();
        for ( int i = 0; i < N; ++i){//распределяю ранги
            tmpRanks[i] = rankCounter;
            ++rankCounter;
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
                if (arr[i] == copyW[j])
                    ranks[i] = tmpRanks[j];
            }
        }
       return ranks;
    }

    public double coefCorrel(double[] arr1, double[] arr2) {
        double xSred = 0, ySred = 0, chisl = 0, znamen, z1 = 0, z2 = 0, coefCorrel;
        for (int i = 0; i < N; ++i) {
            xSred += arr1[i];
            ySred += arr2[i];
        }
        xSred/=N;
        ySred/=N;
        for (int i = 0; i < N; ++i) {
            chisl += ((arr1[i]-xSred)*(arr2[i]-ySred));
            z1 += Math.pow((arr1[i]-xSred),2);
            z2 += Math.pow((arr2[i]-ySred),2);
        }
        znamen = Math.sqrt(z1*z2);
        coefCorrel = chisl/znamen;
        return coefCorrel;
    }


}
