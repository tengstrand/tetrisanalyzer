from tetrisanalyzer.piece.shape import shapes

# https://strategywiki.org/wiki/Tetris/Rotation_systems#Game_Boy_rotation_system
# start_x = 3
# kick = True

O0 = [
    "----",
    "-xx-",
    "-xx-",
    "----",
]

I0 = [
    "----",
    "----",
    "xxxx",
    "----",
]

I1 = [
    "-x--",
    "-x--",
    "-x--",
    "-x--",
]

Z0 = [
    "---",
    "xx-",
    "-xx",
]

Z1 = [
    "-x-",
    "xx-",
    "x--",
]

S0 = [
    "---",
    "-xx",
    "xx-",
]

S1 = [
    "x--",
    "xx-",
    "-x-",
]

J0 = [
    "---",
    "xxx",
    "--x",
]

J1 = [
    "-xx",
    "-x-",
    "-x-",
]

J2 = [
    "x--",
    "xxx",
    "---",
]

J3 = [
    "-x-",
    "-x-",
    "xx-",
]

L0 = [
    "---",
    "xxx",
    "x--",
]

L1 = [
    "-x-",
    "-x-",
    "-xx",
]

L2 = [
    "--x",
    "xxx",
    "---",
]

L3 = [
    "xx-",
    "-x-",
    "-x-",
]

T0 = [
    "---",
    "xxx",
    "-x-",
]

T1 = [
    "-x-",
    "-xx",
    "-x-",
]

T2 = [
    "-x-",
    "xxx",
    "---",
]

T3 = [
    "-x-",
    "xx-",
    "-x-",
]

pieces = [
    [O0],
    [I0, I1],
    [Z0, Z1],
    [S0, S1],
    [J0, J1, J2, J3],
    [L0, L1, L2, L3],
    [T0, T1, T2, T3],
]

shapes = shapes(pieces)
