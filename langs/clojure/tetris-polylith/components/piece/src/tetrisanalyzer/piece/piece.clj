(ns tetrisanalyzer.piece.piece)

(def O 0)
(def I 1)
(def Z 2)
(def S 3)
(def J 4)
(def L 5)
(def T 6)
(def X 8) ;; Outside the board

(defn piece [p rotation shapes]
  (get-in shapes [p rotation]))

(defn set-piece [board p x y piece]
  (reduce (fn [board [cx cy]]
            (assoc-in board [(+ y cy) (+ x cx)] p))
          board
          piece))
