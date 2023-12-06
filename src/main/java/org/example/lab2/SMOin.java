package org.example.lab2;

import java.util.Random;

public class SMOin {
    Random random;
    int  NumServers;
    double MeanServeTime, MeanIATime;


    public SMOin() {
        random = new Random();
//        MeanIATime = random.nextInt(7) + 1;
        MeanIATime = 7;
        MeanServeTime = 10;
//        MeanServeTime = random.nextInt(10) + 1;
        NumServers = 3;
//        NumServers = random.nextInt(12) + 1;
    }

}
