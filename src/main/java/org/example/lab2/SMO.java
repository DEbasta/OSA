package org.example.lab2;

import java.util.ArrayList;
import java.util.Random;

public class SMO {
    Random random;
    final int MaxAllowedInQ = 20;

    int NextEventType, FinishedServer, NumBusy, NumLost, NumServed, MaxNumInQu, NumInQ;

    final double CloseTime = 500;

    double ClockTime, AvgNumInQue, AvgTimeInQue,
            MaxTimeInQu, TotalTimeInQ, TotalTimeBusy, TimeOfLastEvent, SumOfQTimes, TimeSinceLastEvent;

    ArrayList<Double> QTimeArray, TimeOfNextEvent, TimeOfArrival;
    ArrayList<Boolean> EventScheduled;


    SMOin smoIn;

    public SMO() {
        random = new Random();
        smoIn = new SMOin();
        smoIn.MeanIATime = (double) 1 / smoIn.MeanIATime;
        Init();
        do {
            System.out.println("do");
            FindNextEvent();
            UpdateStatistics();
            if (NextEventType == 1) {
                System.out.println("Arrival");
                Arrival();
            } else {
                System.out.println("Departure");
                Departure(FinishedServer);
            }
            System.out.println(ClockTime);
        }
        while ((Math.abs(ClockTime) < CloseTime));
        System.out.println("Report In");
        System.out.println();
        Report();
    }

    public void Init() {
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
        QTimeArray = new ArrayList<>();
        EventScheduled = new ArrayList<>();
        TimeOfNextEvent = new ArrayList<>();
        QTimeArray.add(0.0);
        EventScheduled.add(true);
        TimeOfNextEvent.add(genTimeOfNextEvent());
        for (int i = 1; i < smoIn.NumServers; ++i) {
            EventScheduled.add(false);
        }
    }

    public double genTimeOfNextEvent(){
        Random random = new Random();
        double d;
        d = -smoIn.MeanIATime * Math.log(random.nextInt());
        while (Double.isNaN(d))
            d = -smoIn.MeanIATime * Math.log(random.nextInt());
        return d;
    }

    public void FindNextEvent() {
        double NextEventTime;
        NextEventTime = 10 * CloseTime;
        for (int i=0; i < smoIn.NumServers; ++i) {
            if (EventScheduled.get(i)) {
                if (TimeOfNextEvent.get(i) < NextEventTime)
                    NextEventTime = TimeOfNextEvent.get(i);
                if (i == 0)
                    NextEventType = 1;
                else {
                    NextEventType = 2;
                    FinishedServer = i;
                }
            }
        }
        ClockTime = NextEventTime;
    }

    public void UpdateStatistics() {
        TimeSinceLastEvent = ClockTime - TimeOfLastEvent;
        QTimeArray.set(NumInQ,QTimeArray.get(NumInQ) + TimeSinceLastEvent);
        TotalTimeInQ = TotalTimeInQ + NumInQ * Math.abs(TimeSinceLastEvent);
        TotalTimeBusy = TotalTimeBusy + NumBusy * TimeSinceLastEvent;
        TimeOfLastEvent = ClockTime;
    }

    public void Arrival() {
        TimeOfArrival = new ArrayList<>();
        TimeOfNextEvent.set(0, ClockTime + genTimeOfNextEvent());
        if (TimeOfNextEvent.get(0) > CloseTime)
            EventScheduled.set(0, false);
        if (NumInQ == MaxAllowedInQ) {
            NumLost = NumLost + 1;
            return;
        }

        ArrayList<Double> tmpArr;
        ArrayList<Double> tmpArr2;
        if (NumBusy == smoIn.NumServers) {
            NumInQ = NumInQ + 1;
            System.out.println("num = " + NumInQ);
            if (NumInQ > MaxNumInQu) {
                MaxNumInQu = NumInQ;
                tmpArr = new ArrayList<>(MaxNumInQu + 1);
                tmpArr2 = new ArrayList<>(MaxNumInQu);
                if (QTimeArray.size() > (MaxNumInQu + 1))
                    for (int i = 0; i < (MaxNumInQu + 1); ++i)
                        tmpArr.add(QTimeArray.get(i));
                else {
                    for (int i = 0; i < (MaxNumInQu + 1); ++i)
                        if (i < QTimeArray.size())
                            tmpArr.add(QTimeArray.get(i));
                        else tmpArr.add(null);
                }
                if (TimeOfArrival.size() > MaxNumInQu)
                    for (int i = 0; i < MaxNumInQu; ++i)
                        tmpArr2.add(TimeOfArrival.get(i));
                else {
                    for (int i = 0; i < MaxNumInQu; ++i)
                        if (i < TimeOfArrival.size())
                            tmpArr2.add(TimeOfArrival.get(i));
                        else tmpArr2.add(null);
                }

                QTimeArray = (ArrayList<Double>) tmpArr.clone();
                TimeOfArrival = (ArrayList<Double>) tmpArr2.clone();

                TimeOfArrival.add(NumInQ, ClockTime);
            }
        }
        else {
            NumBusy = NumBusy + 1;
            for (int i = 0; i < smoIn.NumServers; ++i) {
                if (!EventScheduled.get(i)) {
                    EventScheduled.set(i, true);
                    TimeOfNextEvent.add(i,ClockTime + genTimeOfNextEvent());
                    break;
                }
            }
        }

    }
    public void Departure(int FinishedServer){
        double TimeInQ;
        NumServed = NumServed + 1;
        if (NumInQ == 0){
            if (NumBusy > 0)
                NumBusy--;
            else NumBusy = 0;
            EventScheduled.set(FinishedServer, false);
        }
        else {
            NumInQ = NumInQ - 1;
            TimeInQ = ClockTime - TimeOfArrival.get(1);
            if (TimeInQ > MaxTimeInQu) {
                MaxTimeInQu = TimeInQ;
            }

            SumOfQTimes += TimeInQ;
            TimeOfNextEvent.set(FinishedServer,ClockTime + genTimeOfNextEvent());
            for (int i = 1; i < NumInQ; ++i)
                TimeOfArrival.set(i, TimeOfArrival.get(i + 1));
        }
    }


    public void Report() {
        AvgTimeInQue = SumOfQTimes / NumServed;
        AvgNumInQue = TotalTimeInQ / ClockTime;
        System.out.println(TotalTimeInQ);
        System.out.println();
        double AvgServersBusy;
        AvgServersBusy = TotalTimeBusy / ClockTime;

        System.out.println(ClockTime);
        System.out.println(NumServed);
        System.out.println(AvgTimeInQue);
        System.out.println(MaxTimeInQu);
        System.out.println(AvgNumInQue);
        System.out.println(MaxNumInQu);
        System.out.println(AvgServersBusy / smoIn.NumServers);
        System.out.println(NumLost);
        System.out.println(NumLost/(NumLost + NumServed));
    }



}

