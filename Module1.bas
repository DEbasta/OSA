Attribute VB_Name = "Module1"
Option Explicit




Dim MeanIATime As Single, MeanServeTime As Single, NumServers As Integer, _
    MaxAllowedInQ As Integer, CloseTime As Single
    


Dim NumInQ As Integer, NumBusy As Integer, ClockTime As Single, TimeOfLastEvent As Single, _
    EventScheduled() As Boolean, TimeOfNextEvent() As Single


Dim NumServed As Long, NumLost As Integer, MaxNumInQ As Integer, MaxTimeInQ As Single, _
    TimeOfArrival() As Single, TotalTimeInQ As Single, TotalTimeBusy As Single, _
     SumOfQTimes As Single, QTimeArray() As Single


Sub Main()
    Dim NextEventType As Integer, FinishedServer As Integer

    Randomize

    Call ClearOldResults

    MeanIATime = 1 / Range("ArriveRate")
    MeanServeTime = Range("MeanServeTime")
    NumServers = Range("NumServers")
    MaxAllowedInQ = Range("MaxAllowedInQ")
    CloseTime = Range("CloseTime")

    ReDim EventScheduled(NumServers + 1)
    ReDim TimeOfNextEvent(NumServers + 1)

    Call Initialize

    Do

        Call FindNextEvent(NextEventType, FinishedServer)

        Call UpdateStatistics

        If NextEventType = 1 Then
            Call Arrival
        Else
            Call Departure(FinishedServer)
        End If
    Loop Until ClockTime > CloseTime And NumBusy = 0

    Call Report
End Sub

Sub ClearOldResults()
    With Worksheets("������ ���")
        .Range("B12:B23").ClearContents
        With .Range("A26")
            Range(.Offset(1, 0), .Offset(0, 1).End(xlDown)).ClearContents
        End With
    End With
End Sub

Sub Initialize()
    Dim i As Integer

    ClockTime = 0
    NumBusy = 0
    NumInQ = 0
    TimeOfLastEvent = 0

    NumServed = 0
    NumLost = 0
    SumOfQTimes = 0
    MaxTimeInQ = 0
    TotalTimeInQ = 0
    MaxNumInQ = 0
    TotalTimeBusy = 0

    ReDim QTimeArray(1)
    QTimeArray(0) = 0

    EventScheduled(0) = True
    TimeOfNextEvent(0) = -MeanIATime * Log(Rnd)

    For i = 1 To NumServers
        EventScheduled(i) = False
    Next
End Sub

Sub FindNextEvent(NextEventType As Integer, FinishedServer As Integer)
    Dim i As Integer, NextEventTime As Single

    NextEventTime = 10 * CloseTime

    For i = 0 To NumServers
        If EventScheduled(i) Then

            If TimeOfNextEvent(i) < NextEventTime Then
                NextEventTime = TimeOfNextEvent(i)
                If i = 0 Then
                    NextEventType = 1
                Else

                    NextEventType = 2
                    FinishedServer = i

                End If
            End If
        End If
    Next

    ClockTime = NextEventTime
End Sub
      
Sub UpdateStatistics()
    Dim TimeSinceLastEvent As Single

    TimeSinceLastEvent = ClockTime - TimeOfLastEvent

    QTimeArray(NumInQ) = QTimeArray(NumInQ) + TimeSinceLastEvent
    TotalTimeInQ = TotalTimeInQ + NumInQ * TimeSinceLastEvent
    TotalTimeBusy = TotalTimeBusy + NumBusy * TimeSinceLastEvent

    TimeOfLastEvent = ClockTime
End Sub

Sub Arrival()
    Dim i As Integer

    TimeOfNextEvent(0) = ClockTime - MeanIATime * Log(Rnd)

    If TimeOfNextEvent(0) > CloseTime Then
        EventScheduled(0) = False
    End If

    If NumInQ = MaxAllowedInQ Then
        NumLost = NumLost + 1
        Exit Sub
    End If

    If NumBusy = NumServers Then

        NumInQ = NumInQ + 1

        If NumInQ > MaxNumInQ Then
            MaxNumInQ = NumInQ
            ReDim Preserve QTimeArray(MaxNumInQ + 1)
            ReDim Preserve TimeOfArrival(1 To MaxNumInQ)
        End If

        TimeOfArrival(NumInQ) = ClockTime

    Else
        NumBusy = NumBusy + 1

        For i = 1 To NumServers
            If Not EventScheduled(i) Then
                EventScheduled(i) = True
                TimeOfNextEvent(i) = ClockTime - MeanServeTime * Log(Rnd)
                Exit For
            End If
        Next
    End If
End Sub
      
Sub Departure(FinishedServer As Integer)
    Dim TimeInQ As Single, i As Integer

    NumServed = NumServed + 1

    If NumInQ = 0 Then

        If NumBusy > 0 Then    ' ...
        NumBusy = NumBusy - 1
        Else: NumBusy = 0      ' ...
        End If                 ' ...
        
        
        EventScheduled(FinishedServer) = False

    Else

        NumInQ = NumInQ - 1

        TimeInQ = ClockTime - TimeOfArrival(1)

        If TimeInQ > MaxTimeInQ Then
            MaxTimeInQ = TimeInQ
        End If

        SumOfQTimes = SumOfQTimes + TimeInQ

        TimeOfNextEvent(FinishedServer) = ClockTime - MeanServeTime * Log(Rnd)

        For i = 1 To NumInQ
            TimeOfArrival(i) = TimeOfArrival(i + 1)
        Next
    End If
End Sub
      
Sub Report()
    Dim i As Integer, AvgTimeInQ As Single, AvgNumInQ As Single, AvgServersBusy As Single

    AvgTimeInQ = SumOfQTimes / NumServed
    AvgNumInQ = TotalTimeInQ / ClockTime
    AvgServersBusy = TotalTimeBusy / ClockTime

    For i = 0 To MaxNumInQ
        QTimeArray(i) = QTimeArray(i) / ClockTime
    Next

    Range("FinalTime") = ClockTime
    Range("NumServed") = NumServed
    Range("AvgTimeInQ") = AvgTimeInQ
    Range("MaxTimeInQ") = MaxTimeInQ
    Range("AvgNumInQ") = AvgNumInQ
    Range("MaxNumInQ") = MaxNumInQ
    Range("AvgServerUtil") = AvgServersBusy / NumServers
    Range("NumLost") = NumLost
    Range("PctLost").Formula = "=NumLost/(NumLost + NumServed)"

    With Range("A27")
        For i = 0 To MaxNumInQ
            .Offset(i, 0) = i
            .Offset(i, 1) = QTimeArray(i)
        Next
        Range(.Offset(0, 0), .Offset(MaxNumInQ, 0)).Name = "NumInQ"
        Range(.Offset(0, 1), .Offset(MaxNumInQ, 1)).Name = "PctOfTime"
    End With

    Range("A2").Select
End Sub

Sub UpdateChart()
Attribute UpdateChart.VB_ProcData.VB_Invoke_Func = " \n14"
    With Charts("��������� �������������")
        .Visible = True
        .Activate
    End With
    With ActiveChart
        With .SeriesCollection(1)
            .Values = Range("PctOfTime")
            .XValues = Range("NumInQ")
        End With
        .Deselect
    End With
End Sub

Sub ViewChangeInputs()
    Call ClearOldResults
    Call ViewReport
End Sub

Sub ViewReport()
    With Worksheets("������ ���")
        .Visible = True
        .Activate
    End With
    Range("A2").Select
End Sub
Sub CicleN()
Dim i As Integer ', AvgTimeInQ As Single
  If Range("E12") = "" Then
    Range("E12") = 1
  End If
'
  For i = 1 To Range("E12")
    With Worksheets("������ ������").Range("B7")
            .Offset(i, 0) = i
            Call Main
            .Offset(i, 1) = Range("B12") ' 1
            .Offset(i, 2) = Range("B14") ' 2
            .Offset(i, 3) = Range("B15") ' 3
            .Offset(i, 4) = Range("B16") ' 4
            .Offset(i, 5) = Range("B17") ' 5
            .Offset(i, 6) = Range("B19") ' 6
            .Offset(i, 7) = Range("B21") ' 7
            .Offset(i, 8) = Range("B22") ' 8
            .Offset(i, 9) = Range("B23") ' 9
            
    End With
    Next

Worksheets("������ ������").Select
End Sub




