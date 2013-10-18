(ns tetrisanalyzer.board
  (:require [tetrisanalyzer.piece :refer :all])
  (:require [clojure.string :refer (join)]))

(defn empty-line
  ([] (empty-line 8))
  ([width] (vec (flatten (list 9 (repeat (- width 2) 0) 9)))))

(defn bottom-line
  ([] (bottom-line 8))
  ([width] (repeat width 9)))

(defn empty-board
  ([] (empty-board 8 5))
  ([width height]
    (vec (flatten (conj (bottom-line width) (repeat height (empty-line width)))))))

;; Converts from e.g. "#---L--TT#" to [9 0 0 0 5 0 0 6 6 9].
(defn str->row [row]
  (vec (map char->piece row)))

;; Creates a board from a string representation.
(defn str->board [rows]
  (vec (flatten (map str->row rows))))

;; Converts from e.g. [9 0 0 0 5 0 0 6 6 9] to "#---L--TT#".
(defn row->str [row]
  (apply str (map piece->char row)))

;; Creates a board string representation from a board.
(defn board->str [board width]
  (join "\n" (map row->str (partition width board))))

;; Returns a vector that can be used to apply a piece on a board.
;; x,y = position on the board where to put the piece.
;; p = piece, a value in the range 1..7.
;; v = rotation of the piece, 0 = initial rotation
(defn boardpiece [board-width x y p v]
  (vec (flatten (map (fn [[px py]] [(+ x px (* (+ y py) board-width)) p]) (piece-shape p v)))))

;; Sets a piece (p) on the board at position (x,y) with the rotation v.
(defn set-piece-on-board [board-width board x y p v]
  (apply assoc board (boardpiece board-width x y p v)))

;; Converts a list of board lines into a board
;; (convenient method to make the tests more readable).
(defn ++ [& str-board-rows] (str->board (vec str-board-rows)))


;; ------- alternative solution using a two dimentional vector --------

(def board2 [[9 0 0 0 0 0 0 9]
             [9 0 0 0 0 0 0 9]
             [9 0 0 0 0 0 0 9]
             [9 0 0 0 0 0 0 9]
             [9 0 0 0 0 0 0 9]
             [9 9 9 9 9 9 9 9]])

;; ok
(defn prow->xprow [prow px p]
  (reduce (fn [xp x] (conj xp (+ x px) p)) [] prow))


;; ok
(defn set-piece [board x y p piece]
  (reduce (fn [new-board [py xprow]]
          (assoc new-board py (apply assoc (new-board py) xprow))) board
        (map #(vector %2 (prow->xprow % x p)) piece (iterate inc 0))))

(def piece-z [[1 2][0 1]])

(set-piece board2 3 1 5 piece-z)



