from tetrisanalyzer.piece import shape

I = 1
Z = 2
S = 3
J = 4
L = 5
T = 6
O = 7

def piece(p, rotation):
    return shape.pieces[p][rotation]

