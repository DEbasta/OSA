package com.example.lab2;

import java.util.Random;

public class SMO {
    SMOin smoIn;
    Random random;
    final int MaxAllowedInQ = 20;
    final double CloseTime = 500;

    int NextEventType, FinishedServer, NumBusy, NumLost, NumServed, MaxNumInQu, NumInQ;


    double ClockTime, AvgNumInQue, AvgTimeInQue, MaxTimeInQu, TotalTimeInQ, TotalTimeBusy,
            TimeOfLastEvent, SumOfQTimes, AvgServersBusyPercent, NumLostPercent ;

    double[] QTimeArray, TimeOfNextEvent, TimeOfArrival;
    boolean[] EventScheduled;


    public SMO(SMOin smoIn){
        random = new Random();
        this.smoIn = smoIn;
        smoIn.setMeanIATime(1/smoIn.getMeanIATime());
        EventScheduled = new boolean[smoIn.getNumServers() + 1];
        TimeOfNextEvent = new double[smoIn.getNumServers() + 1];

        Init();
        do {
            FindNextEvent ();

            UpdateStatistics();
            if (NextEventType == 1) {
                Arrival();
            }
            else {
                Departure(FinishedServer);
            }
        }
        while ((ClockTime < CloseTime) && (NumBusy >= 0));

        if (ClockTime == 5000)
            ClockTime = 500;
        AvgTimeInQue = SumOfQTimes / NumServed;
        AvgNumInQue = TotalTimeInQ / ClockTime;
        AvgServersBusyPercent = ((TotalTimeBusy / ClockTime)/smoIn.getNumServers())*100;
        NumLostPercent = (double)NumLost/(NumLost + NumServed)*100;
//
//        Report();// вывод результатов смо
    }

    public double genTime(double value){
        Random random = new Random();
        double d;
        d = value * Math.log(random.nextDouble());
        while (Double.isNaN(d))
            d = value * Math.log(random.nextDouble());
        return d;
    }

    public void Init(){
        ClockTime = 0;
        NumBusy = 0;
        NumInQ = 0;
        TimeOfLastEvent = 0;

        NumServed = 0;
        NumLost = 0;
        SumOfQTimes = 0;
        MaxTimeInQu = 0;
        TotalTimeInQ = 0;
        MaxNumInQu = 0;
        TotalTimeBusy = 0;
        QTimeArray = new double[1]; //this could fuck whole thing up
        TimeOfArrival = new double[MaxNumInQu];
        QTimeArray[0] = 0;

        EventScheduled[0] = true;
        TimeOfNextEvent[0] = -genTime(smoIn.getMeanIATime());

        for (int i = 1; i < smoIn.getNumServers(); ++i) {
            EventScheduled[i] = false;
        }
    }

    public void FindNextEvent() {
        double NextEventTime;
        NextEventTime = 10 * CloseTime;

        for (int i=0; i < smoIn.getNumServers(); ++i) {

            if (EventScheduled[i]) {

                if (TimeOfNextEvent[i] < NextEventTime) {
                    NextEventTime = TimeOfNextEvent[i];

                    if (i == 0)
                        NextEventType = 1;
                    else {
                        NextEventType = 2;
                        FinishedServer = i;
                    }
                }
            }
        }
        ClockTime = NextEventTime;
    }

    public void UpdateStatistics() {
        double TimeSinceLastEvent;
        TimeSinceLastEvent = ClockTime - TimeOfLastEvent;

        QTimeArray[NumInQ] = QTimeArray[NumInQ] + TimeSinceLastEvent;
        TotalTimeInQ = TotalTimeInQ + NumInQ * TimeSinceLastEvent;
        TotalTimeBusy = TotalTimeBusy + NumBusy * TimeSinceLastEvent;

        TimeOfLastEvent = ClockTime;
    }

    public void PreserveQTimeArray(){
        double [] tmpArr = new double[MaxNumInQu + 1];
        System.arraycopy(QTimeArray, 0, tmpArr, 0, QTimeArray.length);
        QTimeArray = tmpArr;
    }

    public void PreserveTimeOfArrival(){
        double [] tmpArr = new double[MaxNumInQu];
        for (int i = 1; i < TimeOfArrival.length;++i) {
            tmpArr[i-1] = TimeOfArrival[i];
        }
        TimeOfArrival = tmpArr;
    }

    public void Arrival() {
        TimeOfNextEvent[0] = ClockTime - genTime(smoIn.getMeanIATime());

        if (TimeOfNextEvent[0] > CloseTime ) {
            EventScheduled[0] = false;
        }

        if (NumInQ == MaxAllowedInQ ) {
            NumLost = NumLost + 1;
            return;
        }

        if (NumBusy == smoIn.getNumServers()) {

            NumInQ = NumInQ + 1;

            if (NumInQ > MaxNumInQu) {
                MaxNumInQu = NumInQ;
                PreserveQTimeArray();
                PreserveTimeOfArrival();
            }

            TimeOfArrival[NumInQ-1] = ClockTime;
        }

        else {
            NumBusy = NumBusy + 1;

            for (int i = 1; i < smoIn.getNumServers(); ++i) {
                if (!EventScheduled[i]) {
                    EventScheduled[i] = true;
                    TimeOfNextEvent[i] = ClockTime - genTime(smoIn.getMeanServeTime());
                    break;
                }
            }

        }
    }

    public void Departure(int FinishedServer){
        double TimeInQ;
        NumServed = NumServed + 1;
        if (NumInQ == 0) {

            if (NumBusy > 0)
                NumBusy = NumBusy - 1;
            else
                NumBusy = 0;
            EventScheduled[FinishedServer] = false;
        }
        else {
            NumInQ = NumInQ - 1;

            TimeInQ = ClockTime - TimeOfArrival[0];

            if (TimeInQ > MaxTimeInQu) {
                MaxTimeInQu = TimeInQ;
            }

            SumOfQTimes = SumOfQTimes + TimeInQ;

            TimeOfNextEvent[FinishedServer] = ClockTime - genTime(smoIn.getMeanServeTime());
        }

        for (int i = 1; i < NumInQ; ++i) {
            TimeOfArrival[i] = TimeOfArrival[i + 1];
        }
    }


    public void Report() {
        System.out.println();
        System.out.println("1. Time of last req " + ClockTime);
        System.out.println("2. Avg Time in Q " + AvgTimeInQue);
        System.out.println("3. Max Time in Q " + MaxTimeInQu);
        System.out.println("4. Avg Number In Q " + AvgNumInQue);
        System.out.println("5. Max Number In Q " + MaxNumInQu);
        System.out.println("6. Avg Servers Busy Percent " + AvgServersBusyPercent);
        System.out.println("7. Number of Served " + NumServed);
        System.out.println("8. Number of Lost " + NumLost);
        System.out.println("9. Percent of Lost " + NumLostPercent);
    }

    public double getClockTime() {
        return ClockTime;
    }

    public double getAvgTimeInQue() {
        return AvgTimeInQue;
    }

    public double getMaxTimeInQu() {
        return MaxTimeInQu;
    }

    public double getAvgNumInQue() {
        return AvgNumInQue;
    }

    public int getMaxNumInQu() {
        return MaxNumInQu;
    }

    public double getAvgServersBusyPercent() {
        return AvgServersBusyPercent;
    }

    public int getNumServed() {
        return NumServed;
    }

    public int getNumLost() {
        return NumLost;
    }

    public double getNumLostPercent() {
        return NumLostPercent;
    }
}
