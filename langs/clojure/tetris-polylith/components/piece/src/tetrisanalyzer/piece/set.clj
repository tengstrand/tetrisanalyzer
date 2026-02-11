(ns tetrisanalyzer.piece.set)

(defn set-cell [board p x y [cx cy]]
  (assoc-in board [(+ y cy) (+ x cx)] p))

(defn set-piece [board p x y piece]
  (reduce (fn [board cell]
            (set-cell board p x y cell))
          board
          piece))
