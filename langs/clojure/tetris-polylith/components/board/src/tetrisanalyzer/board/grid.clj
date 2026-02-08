(ns tetrisanalyzer.board.grid)

(defn row [grid-row]
  (mapv #(if (= % \-) 0 1)
        (str grid-row)))

(defn board [grid-board]
  (mapv row grid-board))
