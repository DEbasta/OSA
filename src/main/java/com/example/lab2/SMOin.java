package com.example.lab2;

import java.util.Random;

public class SMOin {
    Random random;
    int  NumServers;
    double MeanServeTime, MeanIATime;


    public SMOin() {
        random = new Random();
//        MeanIATime = 7;
//        MeanServeTime = 10;
//        NumServers = 3;
        MeanIATime = random.nextInt(7) + 1;
        MeanServeTime = random.nextInt(10) + 1;
        NumServers = random.nextInt(12) + 1;
    }

    public int getNumServers() {
        return NumServers;
    }

    public double getMeanServeTime() {
        return MeanServeTime;
    }

    public double getMeanIATime() {
        return MeanIATime;
    }

    public void setMeanIATime(double meanIATime) {
        MeanIATime = meanIATime;
    }
}
