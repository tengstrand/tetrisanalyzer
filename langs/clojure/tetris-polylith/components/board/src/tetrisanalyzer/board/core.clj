(ns tetrisanalyzer.board.core)

(defn width [board]
  (-> board first count))

(defn height [board]
  (count board))

(defn empty-row [width]
  (vec (repeat width 0)))

(defn empty-board [width height]
  (vec (repeat height (empty-row width))))

(defn set-cell [board p x y [cx cy]]
  (assoc-in board [(+ y cy) (+ x cx)] p))

(defn set-piece [board p x y piece]
  (reduce (fn [board cell]
            (set-cell board p x y cell))
          board
          piece))
