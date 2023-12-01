Attribute VB_Name = "Module1"
Option Explicit

' Объявление параметров системы:
'   MeanIATime - среднее время между прибытием клиентов (аналог
'   скорости прибытия клиентов)
'   MeanServeTime - среднее время обслуживания
'   NumServers - количество служащих
'   MaxAllowedInQ - максимальное количество клиентов в очереди
'   CloseTime - время, после которого вновь прибывшие клиенты
'   не обслуживаются


Dim MeanIATime As Single, MeanServeTime As Single, NumServers As Integer, _
    MaxAllowedInQ As Integer, CloseTime As Single
    
' Объявление системных индикаторов:
'   NumInQ - количество клиентов в очереди
'   NumBusy - количество занятых служащих
'   ClockTime - текущее время (начальное время равно нулю)
'   TimeOfLastEvent - время наступления предыдущего события
'   EventScheduled(i) - True или False, в зависимости от
'   планирования события типа i (i>=0), где i=0 соответствует
'   прибытию клиента, а i от 1 до NumServers — завершению его
'   обслуживания служащим i
'   TimeOfNextEvent(i) - время наступления следующего
'   запланированного события типа i (только в случае равенства
'   EventScheduled(i) значению True

Dim NumInQ As Integer, NumBusy As Integer, ClockTime As Single, TimeOfLastEvent As Single, _
    EventScheduled() As Boolean, TimeOfNextEvent() As Single

' Объявление статистических показателей
'   NumServed - количество обслуженных клиентов
'   NumLost - количество клиентов, которые не были обслужены
'   (очередь полностью заполнена)
'   MaxNumInQ - максимальное количество клиентов в очереди на
'   протяжении всего времени обслуживания
'   MaxTimeInQ - максимальное время, проведенное клиентом в очереди
'   TimeOfArrival(i) - время прибытия клиента, занесенного
'   в очередь под номером i, для i>=1
'   TotalTimeInQ - общее количество минут, проведенных каждым
'   клиентом в очереди
'   TotalTimeBusy - общее количество минут, на протяжении которых
'   обслуживались все клиенты (работали все служащие)
'   SumOfQTimes - общее количество минут, проведенных всеми
'   клиентами в очереди
'   QTimeArray(i) - время, на протяжении которого в очереди
'   было i клиентов (для i>=0)

Dim NumServed As Long, NumLost As Integer, MaxNumInQ As Integer, MaxTimeInQ As Single, _
    TimeOfArrival() As Single, TotalTimeInQ As Single, TotalTimeBusy As Single, _
     SumOfQTimes As Single, QTimeArray() As Single


Sub Main()
    Dim NextEventType As Integer, FinishedServer As Integer

' Генерация новых случайных чисел при каждом запуске
    Randomize

' Удаление старых результатов из листа Модель СМО
    Call ClearOldResults

' Получение входных данных из листа Модель СМО
    MeanIATime = 1 / Range("ArriveRate")
    MeanServeTime = Range("MeanServeTime")
    NumServers = Range("NumServers")
    MaxAllowedInQ = Range("MaxAllowedInQ")
    CloseTime = Range("CloseTime")

    ReDim EventScheduled(NumServers + 1)
    ReDim TimeOfNextEvent(NumServers + 1)

' Установка счетчиков, индикаторов в значение нуль и планирование первого события
    Call Initialize

' Имитация модели до обслуживания последнего клиента
    Do

' Определения времени и типа следующего события, обновление часов.
' Получение индекса освободившегося служащего (если следующее
' событие - завершение обслуживания)
        Call FindNextEvent(NextEventType, FinishedServer)

' Обновление статистических показателей
        Call UpdateStatistics

' NextEventType равно 1 для прибытия клиента и 2 - для
' завершения его обслуживания
        If NextEventType = 1 Then
            Call Arrival
        Else
            Call Departure(FinishedServer)
        End If
    Loop Until ClockTime > CloseTime And NumBusy = 0

' Вывод результатов
    Call Report
End Sub

Sub ClearOldResults()
    With Worksheets("Модель СМО")
        .Range("B12:B23").ClearContents
        With .Range("A26")
            Range(.Offset(1, 0), .Offset(0, 1).End(xlDown)).ClearContents
        End With
    End With
End Sub

Sub Initialize()
    Dim i As Integer

' Инициализация системных индикаторов
    ClockTime = 0
    NumBusy = 0
    NumInQ = 0
    TimeOfLastEvent = 0

' Инициализация статистических переменных
    NumServed = 0
    NumLost = 0
    SumOfQTimes = 0
    MaxTimeInQ = 0
    TotalTimeInQ = 0
    MaxNumInQ = 0
    TotalTimeBusy = 0

' Изменение размера массива QTimeArray (содержит только один
' элемент для времени, когда в очереди нет клиентов)
    ReDim QTimeArray(1)
    QTimeArray(0) = 0

' Создание расписания случайного прибытия клиентов
    EventScheduled(0) = True
    TimeOfNextEvent(0) = -MeanIATime * Log(Rnd)

' Исходно клиентов в очереди нет, поэтому завершение обслуживания не планируется
    For i = 1 To NumServers
        EventScheduled(i) = False
    Next
End Sub

Sub FindNextEvent(NextEventType As Integer, FinishedServer As Integer)
    Dim i As Integer, NextEventTime As Single

' NextEventTime определяет следующее наступающее событие.
' Вначале ей определяется большое значение
    NextEventTime = 10 * CloseTime

' Определение типа и времени наступления следующего
' запланированного события. Потенциально может произойти событие
' прибытия нового клиента (индекс0), или завершиться обслуживание
' одного из клиентов (индекс от 1 до NumServers)
    For i = 0 To NumServers
        If EventScheduled(i) Then

' Сохранение следующего события
            If TimeOfNextEvent(i) < NextEventTime Then
                NextEventTime = TimeOfNextEvent(i)
                If i = 0 Then
                    NextEventType = 1
                Else

' Для события завершения обслуживания сохраняется индекс освободившегося служащего
                    NextEventType = 2
                    FinishedServer = i

                End If
            End If
        End If
    Next

' Обновление часов временем следующего события
    ClockTime = NextEventTime
End Sub
      
Sub UpdateStatistics()
    Dim TimeSinceLastEvent As Single

' TimeSinceLastEvent - время, прошедшее с момента наступления предыдущего события
    TimeSinceLastEvent = ClockTime - TimeOfLastEvent

' Обновление статистических данных
    QTimeArray(NumInQ) = QTimeArray(NumInQ) + TimeSinceLastEvent
    TotalTimeInQ = TotalTimeInQ + NumInQ * TimeSinceLastEvent
    TotalTimeBusy = TotalTimeBusy + NumBusy * TimeSinceLastEvent

' Обновление переменной TimeOfLastEvent текущим временем
    TimeOfLastEvent = ClockTime
End Sub

Sub Arrival()
    Dim i As Integer

' Планирование прибытия следующего клиента
    TimeOfNextEvent(0) = ClockTime - MeanIATime * Log(Rnd)

' Отмена события прибытия, если текущее время превышает рабочее время заведения
    If TimeOfNextEvent(0) > CloseTime Then
        EventScheduled(0) = False
    End If

' Если очередь заполнена, то клиенту отказывают в обслуживании
    If NumInQ = MaxAllowedInQ Then
        NumLost = NumLost + 1
        Exit Sub
    End If

' Проверка занятости всех служащих
    If NumBusy = NumServers Then

' Если все служащие заняты, то клиент добавляется в конец очереди
        NumInQ = NumInQ + 1

' Если очередь увеличилась, то переменная MaxNumInQ обновляется,
' а массивы изменяют свой размер
        If NumInQ > MaxNumInQ Then
            MaxNumInQ = NumInQ
            ReDim Preserve QTimeArray(MaxNumInQ + 1)
            ReDim Preserve TimeOfArrival(1 To MaxNumInQ)
        End If

' Сохранение времени прибытия клиента (используется дальше)
        TimeOfArrival(NumInQ) = ClockTime

    Else
' Клиент немедленно обслуживается, поэтому количество занятых служащих увеличивается
        NumBusy = NumBusy + 1

' Поиск незанятого служащего и планирование для него события завершения обслуживания
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

' Обновление количества обслуженных клиентов
    NumServed = NumServed + 1

' Проверка ожидающих в очереди клиентов
    If NumInQ = 0 Then

' Очереди нет, поэтому служащий, завершивший работу со своим
' клиентом, считается незанятым
                
        'Range("E1") = NumBusy  ' ...
        If NumBusy > 0 Then    ' ...
        NumBusy = NumBusy - 1
        Else: NumBusy = 0      ' ...
        End If                 ' ...
        
        
        EventScheduled(FinishedServer) = False

    Else

' В очереди есть клиенты, следующий клиент в очереди обслуживается
        NumInQ = NumInQ - 1

' TimeInQ - время ожидания текущего клиента в очереди
        TimeInQ = ClockTime - TimeOfArrival(1)

' Проверка времени ожидания на максимальность
        If TimeInQ > MaxTimeInQ Then
            MaxTimeInQ = TimeInQ
        End If

' Обновление общего времени ожидания клиентами в очереди
        SumOfQTimes = SumOfQTimes + TimeInQ

' Планирование обслуживания клиента освободившимся служащим
        TimeOfNextEvent(FinishedServer) = ClockTime - MeanServeTime * Log(Rnd)

' Перемещение клиентов в очереди
        For i = 1 To NumInQ
            TimeOfArrival(i) = TimeOfArrival(i + 1)
        Next
    End If
End Sub
      
Sub Report()
    Dim i As Integer, AvgTimeInQ As Single, AvgNumInQ As Single, AvgServersBusy As Single

' Расчет показателей
    AvgTimeInQ = SumOfQTimes / NumServed
    AvgNumInQ = TotalTimeInQ / ClockTime
    AvgServersBusy = TotalTimeBusy / ClockTime

' Запись массива QTimeArray, от 0 до MaxNumInQ, среднее время,
' которое каждый клиент провел в очереди
    For i = 0 To MaxNumInQ
        QTimeArray(i) = QTimeArray(i) / ClockTime
    Next

' Вывод конечных результатов и именование диапазонов
    Range("FinalTime") = ClockTime
    Range("NumServed") = NumServed
    Range("AvgTimeInQ") = AvgTimeInQ
    Range("MaxTimeInQ") = MaxTimeInQ
    Range("AvgNumInQ") = AvgNumInQ
    Range("MaxNumInQ") = MaxNumInQ
    Range("AvgServerUtil") = AvgServersBusy / NumServers
    Range("NumLost") = NumLost
    Range("PctLost").Formula = "=NumLost/(NumLost + NumServed)"

' Распределение длины очереди на основе данных под строкой 27
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
    With Charts("Диаграмма распределения")
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
    With Worksheets("Модель СМО")
        .Visible = True
        .Activate
    End With
    Range("A2").Select
End Sub
':My: Циклическое проведение эксперимента N раз (без изменения параметров СМО) -------------
Sub CicleN()
Dim i As Integer ', AvgTimeInQ As Single
  If Range("E12") = "" Then
    Range("E12") = 1
  End If
'
  For i = 1 To Range("E12")
    With Worksheets("Анализ модели").Range("B7")
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

'  With Range("A27")
'        For i = 0 To MaxNumInQ
'            .Offset(i, 0) = i
'            .Offset(i, 1) = QTimeArray(i)
'        Next
'        Range(.Offset(0, 0), .Offset(MaxNumInQ, 0)).Name = "NumInQ"
'        Range(.Offset(0, 1), .Offset(MaxNumInQ, 1)).Name = "PctOfTime"
'    End With

'    With Worksheets("Модель СМО")
'        .Range("B12:B23").ClearContents
'        With .Range("A26")
'            Range(.Offset(1, 0), .Offset(0, 1).End(xlDown)).ClearContents
'        End With
'    End With
' ... Теперь переключаемся на лист Анализ модели
Worksheets("Анализ модели").Select

End Sub
':My: Очистка Моделирования N раз -------------
Sub ClearN()
Dim i As Integer
  For i = 1 To Range("E12")
    With Worksheets("Анализ модели").Range("B7")
        .Offset(i, 0).ClearContents
            .Offset(i, 1).ClearContents ' 1
            .Offset(i, 2).ClearContents ' 2
            .Offset(i, 3).ClearContents ' 3
            .Offset(i, 4).ClearContents ' 4
            .Offset(i, 5).ClearContents ' 5
            .Offset(i, 6).ClearContents ' 6
            .Offset(i, 7).ClearContents ' 7
            .Offset(i, 8).ClearContents ' 8
            .Offset(i, 9).ClearContents ' 9
    
    End With
    Next
' ... Теперь переключаемся на лист Анализ модели
Worksheets("Анализ модели").Select

End Sub

