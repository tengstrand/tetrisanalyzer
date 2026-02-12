from tetrisanalyzer.piece.visit import is_visited, visit

X = 2
Y = 1
ROTATION = 3
UNVISITED = [
    [0, 0, 0, 0],
    [0, 0, 0, 0],
]


def test_move_is_not_visited():
    assert is_visited(UNVISITED, X, Y, ROTATION) is False


def test_move_is_visited():
    visited = [row[:] for row in UNVISITED]
    visit(visited, X, Y, ROTATION)
    assert is_visited(visited, X, Y, ROTATION) is True
