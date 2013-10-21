(ns tetrisanalyzer.core
  (use [clojure.string :only (join)])  )

;; ===== Pieces =====

;; I (1)
(def I [[[0 0] [1 0] [2 0] [3 0]]
        [[0 0] [0 1] [0 2] [0 3]]])

;; Z (2)
(def Z [[[1 0] [2 0] [0 1] [1 1]]
        [[1 0] [0 1] [1 1] [0 2]]])

;; S (3)
(def S [[[1 0] [2 0] [0 1] [1 1]]
        [[0 0] [0 1] [1 1] [1 2]]])

;; J (4)
(def J [[[0 0] [1 0] [2 0] [2 1]]
        [[0 0] [1 0] [0 1] [0 2]]
        [[0 0] [0 1] [1 1] [2 1]]
        [[1 0] [1 1] [0 2] [1 2]]])

;; L (5)
(def L [[[0 0] [1 0] [2 0] [0 1]]
        [[0 0] [0 1] [0 2] [1 2]]
        [[2 0] [0 1] [1 1] [2 1]]
        [[0 0] [1 0] [1 1] [1 2]]])

;; T (6)
(def T [[[0 0] [1 0] [2 0] [1 1]]
        [[0 0] [0 1] [1 1] [0 2]]
        [[1 0] [0 1] [1 1] [2 1]]
        [[1 0] [0 1] [1 1] [1 2]]])

;; O (7)
(def O [[[0 0] [1 0] [0 1] [1 1]]])

;; Represents the pieces with index 1..7.
(def pieces [nil I Z S J L T O])

(def char->piece {\- 0 \I 1 \Z 2 \S 3 \J 4 \L 5 \T 6 \O 7 \x 8 \# 9})

(defn piece->char [piece] (nth "-IZSJLTOx#" piece))

(defn rotate-and-move-piece [p rotation x y]
  (mapcat (fn [[px py]] [[(+ y py) (+ x px)] p]) ((pieces p) rotation)))

;; ===== board =====

(defn- row->str [row]
  (apply str (map (fn [[_ p]] (piece->char p)) row)))

(defn board->str [board width]
  (join "\n" (map row->str (partition width (sort board)))))

(defn set-piece [board p rotation x y]
  (apply assoc board (rotate-and-move-piece p rotation x y)))

(defn- str->row [row y]
  (map #(vector [y %2] (char->piece %1)) row (range)))

(defn new-board
  ([] (new-board 12 21))
  ([rows] (mapcat #(str->row %1 %2) rows (range)))
  ([width height]
    (into {} (for [y (range height) x (range width)
                   :let [wall? (or (= (- height 1) y) (zero? x) (= (- width 1) x))]]
                   [[y x] (if wall? 9 0)]))))
