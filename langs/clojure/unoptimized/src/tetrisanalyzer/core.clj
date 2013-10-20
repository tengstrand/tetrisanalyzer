(ns tetrisanalyzer.core
  (use [clojure.string :only (join)])  )

;; ===== Pieces =====

;; I (1)
(def I0 [[0 0][1 0][2 0][3 0]])
(def I1 [[0 0][0 1][0 2][0 3]])
(def I [I0 I1])

;; Z (2)
(def Z0 [[1 0] [2 0] [0 1] [1 1]])
(def Z1 [[1 0][0 1][1 1][0 2]])
(def Z [Z0 Z1])

;; S (3)
(def S0 [[1 0][2 0][0 1][1 1]])
(def S1 [[0 0][0 1][1 1][1 2]])
(def S [S0 S1])

;; J (4)
(def J0 [[0 0][1 0][2 0][2 1]])
(def J1 [[0 0][1 0][0 1][0 2]])
(def J2 [[0 0][0 1][1 1][2 1]])
(def J3 [[1 0][1 1][0 2][1 2]])
(def J [J0 J1 J2 J3])

;; L (5)
(def L0 [[0 0][1 0][2 0][0 1]])
(def L1 [[0 0][0 1][0 2][1 2]])
(def L2 [[2 0][0 1][1 1][2 1]])
(def L3 [[0 0][1 0][1 1][1 2]])
(def L [L0 L1 L2 L3])

;; T (6)
(def T0 [[0 0][1 0][2 0][1 1]])
(def T1 [[0 0][0 1][1 1][0 2]])
(def T2 [[1 0][0 1][1 1][2 1]])
(def T3 [[1 0][0 1][1 1][1 2]])
(def T [T0 T1 T2 T3])

;; O (7)
(def O0 [[0 0][1 0][0 1][1 1]])
(def O [O0])

;; Represents the pieces with index 1..7.
(def pieces [nil I Z S J L T O])

(def char->piece {\- 0 \I 1 \Z 2 \S 3 \J 4 \L 5 \T 6 \O 7 \x 8 \# 9})

(defn piece->char [piece] (nth "-IZSJLTOx#" piece))

(defn move-piece [p v x y]
  (mapcat (fn [[px py]] [[(+ y py) (+ x px)] p]) ((pieces p) v)))

;; ===== board =====

(defn- row->str [row]
  (apply str (map (fn [[_ p]] (piece->char p)) row)))

(defn board->str [board width]
  (join "\n" (map row->str (partition width (sort board)))))

(defn set-piece [board p v x y]
  (apply assoc board (move-piece p v x y)))

(defn- str->row [row y]
  (map #(vector [y %2] (char->piece %)) row (range)))

(defn new-board
  ([] (new-board 12 21))
  ([rows] (mapcat #(str->row % %2) rows (range)))
  ([width height]
    (into {} (for [y (range height) x (range width)
                   :let [is-wall (or (= (- height 1) y) (= x 0) (= (- width 1) x))]]
                   [[y x] (if is-wall 9 0)]))))
