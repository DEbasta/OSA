package org.example.lab2;

import java.util.ArrayList;
import java.util.Random;

public class SMO {
    Random random;
    final int MaxAllowedInQ = 20;

    int NextEventType,FinishedServer, NumBusy, NumLost, NumServed, MaxNumInQ, NumInQ;

    final double CloseTime = 500;

    double ClockTime, AvgNumInQ, AvgTimeInQ,
    MaxTimeInQ, TotalTimeInQ, TotalTimeBusy, TimeOfLastEvent, SumOfQTimes, TimeSinceLastEvent;

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
            }
            else {
                System.out.println("Departure");
                Departure(FinishedServer);
            }
            System.out.println(ClockTime);
        }
        while ((ClockTime > CloseTime) );
        System.out.println("Report In");
        Report();
    }

    public void Init() {
        NumBusy = 0;
        NumInQ = 0;
        TimeOfLastEvent = 0;
        NumServed = 0;
        NumLost = 0;
        SumOfQTimes = 0;
        MaxTimeInQ = 0;
        TotalTimeInQ = 0;
        MaxNumInQ = 0;
        TotalTimeBusy = 0;
        QTimeArray = new ArrayList<>();
        EventScheduled = new ArrayList<>();
        TimeOfNextEvent = new ArrayList<>();
        QTimeArray.add(0.0);
        EventScheduled.add(true);
        TimeOfNextEvent.add(Math.abs(-smoIn.MeanIATime * Math.log(random.nextInt())));
        for (int  i = 1; i < smoIn.NumServers; ++i) {
            EventScheduled.add(false);
        }
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
        TotalTimeInQ = TotalTimeInQ + NumInQ * TimeSinceLastEvent;
        TotalTimeBusy = TotalTimeBusy + NumBusy * TimeSinceLastEvent;
        TimeOfLastEvent = ClockTime;
    }
    public void Arrival() {
        TimeOfArrival = new ArrayList<>();
        TimeOfNextEvent.set(0,Math.abs(ClockTime - smoIn.MeanIATime * Math.log(random.nextInt())));
        if (TimeOfNextEvent.get(0) > CloseTime)
            EventScheduled.set(0,false);
        if (NumInQ == MaxAllowedInQ) {
            NumLost = NumLost + 1;
            return;
        }
        ArrayList<Double> tmpArr;
        ArrayList<Double> tmpArr2;
        if (NumBusy == smoIn.NumServers) {
            NumInQ = NumInQ + 1;
            if (NumInQ > MaxNumInQ) {
                MaxNumInQ = NumInQ;
                tmpArr = new ArrayList<>(MaxNumInQ + 1);
                tmpArr2 = new ArrayList<>(MaxNumInQ);
                if (QTimeArray.size() > (MaxNumInQ + 1))
                    for (int i = 0; i < (MaxNumInQ + 1); ++i)
                        tmpArr.add(QTimeArray.get(i));
                else {
                    for (int i = 0; i < (MaxNumInQ + 1); ++i)
                        if (i < QTimeArray.size())
                            tmpArr.add(QTimeArray.get(i));
                        else tmpArr.add(null);
                }
                if (TimeOfArrival.size() > MaxNumInQ)
                    for (int i = 0; i < MaxNumInQ; ++i)
                        tmpArr2.add(TimeOfArrival.get(i));
                else {
                    for (int i = 0; i < MaxNumInQ; ++i)
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
                    TimeOfNextEvent.add(i,Math.abs(ClockTime - smoIn.MeanServeTime * Math.log(random.nextInt())));
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
            if (TimeInQ > MaxTimeInQ) {
                MaxTimeInQ = TimeInQ;
            }

            SumOfQTimes += TimeInQ;
            TimeOfNextEvent.set(FinishedServer,Math.abs(ClockTime - smoIn.MeanServeTime * Math.log(random.nextInt())));
            for (int i = 1; i < NumInQ; ++i)
                TimeOfArrival.set(i, TimeOfArrival.get(i + 1));
        }
    }

    public void Report() {
        AvgTimeInQ = SumOfQTimes / NumServed;
        AvgNumInQ = TotalTimeInQ / ClockTime;
        double AvgServersBusy;
        AvgServersBusy = TotalTimeBusy / ClockTime;

        for (int i = 0; i < MaxNumInQ; ++i){
            QTimeArray.set(i, QTimeArray.get(i) / ClockTime);
        }

        System.out.println(ClockTime);
        System.out.println(NumServed);
        System.out.println(AvgTimeInQ);
        System.out.println(MaxTimeInQ);
        System.out.println(AvgNumInQ);
        System.out.println(MaxNumInQ);
        System.out.println(AvgServersBusy / smoIn.NumServers);
        System.out.println(NumLost);
        System.out.println(NumLost/(NumLost + NumServed));
    }
}
