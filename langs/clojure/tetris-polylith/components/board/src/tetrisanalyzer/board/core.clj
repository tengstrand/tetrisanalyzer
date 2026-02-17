(ns tetrisanalyzer.board.core)

(defn width [board]
  (-> board first count))

(defn height [board]
  (count board))

(defn empty-row [width]
  (vec (repeat width 0)))

(defn empty-board [width height]
  (vec (repeat height (empty-row width))))

