import math
from array import array

from scipy.optimize import differential_evolution

text_file = open("x1.txt", "r")
x1 = text_file.read().split(' ')
text_file = open("x2.txt", "r")
x2 = text_file.read().split(' ')
text_file = open("x3.txt", "r")
x3 = text_file.read().split(' ')
text_file = open("x4.txt", "r")
x4 = text_file.read().split(' ')
text_file = open("x5.txt", "r")
x5 = text_file.read().split(' ')
text_file = open("x6.txt", "r")
x6 = text_file.read().split(' ')
text_file = open("x7.txt", "r")
x7 = text_file.read().split(' ')
text_file = open("x8.txt", "r")
x8 = text_file.read().split(' ')
text_file = open("x9.txt", "r")
x9 = text_file.read().split(' ')
text_file = open("y1.txt", "r")
y1 = text_file.read().split(' ')
text_file = open("y2.txt", "r")
y2 = text_file.read().split(' ')



d = array('d', [1.0, 2.0, 3.14])
x0_6 = array('d', [1.0, 2.0, 3.14])
x1_6 = array('d', [1.0, 2.0, 3.14])
x2_6 = array('d', [25.0, 2.0, 3.14])
k=3

def fobj(v):
    af, bf, cf = v
    value = 0
    for i in range(k):
        value += math.pow((d[i] - (af * x0_6[i] + bf * x1_6[i] + cf + x2_6[i])), 2)
    return value

def RcubeLinear(flag, solution):
    print(solution)
    ch = 0
    zn = 0
    srzn = 0
    if flag:
        for i in range(30):
            srzn += float(y1[i])
        srzn /= 30
        for i in range(30):
            print(f(solution,i))
            ch+=math.pow(f(solution,i)-float(y1[i]),2)
            zn+=math.pow(float(y1[i]) - srzn,2)
    else:
        for i in range(30):
            srzn += float(y2[i])
        srzn /= 30
        for i in range(30):
            ch+=math.pow(f(solution,i)-float(y2[i]),2)
            zn+=math.pow(float(y2[i]) - srzn,2)
    print(srzn)
    print(ch)
    print(zn)
    value = 1 - ch/zn
    return value
def f1(v):#для нахождения коэфицентов a0,a1,a2,a3
    a0, a1, a2, a3 = v
    result = 0
    for i in range(30):
        result += (a0 + a1 * float(x1[i]) + a2 * float(x2[i]) + a3 * float(x3[i]))
    return result
def f(solution,i):
    a0, a1, a2, a3 = solution
    return a0 + a1 * float(x1[i]) + a2 * float(x2[i]) + a3 * float(x3[i])

r_min, r_max = -500.0, 500.0
bounds = [[r_min, r_max], [r_min, r_max], [r_min, r_max],  [r_min, r_max]]
result = differential_evolution(f1, bounds)
print('Status : %s' % result['message'])
solution = result.x
print(RcubeLinear(True, solution))
print(RcubeLinear(False, solution))



# r_min, r_max = -1000.0, 1000.0
# bounds = [[r_min, r_max], [r_min, r_max], [r_min, r_max]]
# result = differential_evolution(fobj, bounds)
# print('Status : %s' % result['message'])
# print('Total Evaluations: %d' % result.fun)
# solution = result['x']
# evaluation = fobj(solution)
# print('Solution: f(%s) = %.5f' % (solution, evaluation))
#FL = a0 + a1x1 + a2x2 + a3x3
#FNL =  a0 + a1x1 + a2x2 + a3x3 + a11x1^2 + a12x1x2 + a13x1x3 + a22x2^2 + a23x2x3 + a33x3^2
