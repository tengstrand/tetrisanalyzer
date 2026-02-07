# The mapping between number of shapes/rotations and the rotation bitmask.
_NUM_SHAPES_TO_BITMASK = {
    1: 0,  # 00 = always 0
    2: 1,  # 01 = cycles between 0 and 1
    4: 3,  # 11 = cycles between 0, 1, 2, and 3
}


def rotation_bitmask(shapes, p):
    return _NUM_SHAPES_TO_BITMASK[len(shapes[p])]
