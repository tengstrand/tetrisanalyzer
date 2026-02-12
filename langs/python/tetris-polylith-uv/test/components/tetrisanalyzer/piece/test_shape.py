from tetrisanalyzer.piece.shape import shape


def test_converts_a_piece_shape_grid_to_a_list_of_xy_cells():
    assert [[2, 0],
            [1, 1],
            [2, 1],
            [1, 2]] == shape(
        ["--x-",
         "-xx-",
         "-x--",
         "----"]
    )
