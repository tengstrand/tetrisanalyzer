(ns tetrisanalyzer.board)

(use '[clojure.string :only (join)])
(use '[tetrisanalyzer.piece])

(defn- empty-line
  ([] (empty-line 8))
  ([width] (vec (flatten (list 9 (repeat (- width 2) 0) 9)))))

(defn- bottom-line
  ([] (bottom-line 8))
  ([width] (repeat width 9)))

(defn empty-board
  ([] (empty-board 8 5))
  ([width height]
    (vec (flatten (conj (bottom-line width) (repeat height (empty-line width)))))))

;; Converts from e.g. "#---L--TT#" to [9 0 0 0 5 0 0 6 6 9].
(defn- str->row [row]
  (vec (map #(char->piece %) row)))

;; Creates a board from a string representation.
(defn str->board
  ([rows]
    (vec (flatten (map #(str->row %) rows)))))

;; Converts from e.g. [9 0 0 0 5 0 0 6 6 9] to "#---L--TT#".
(defn- row->str [row]
  (apply str (map #(piece->char %) row)))

;; Creates a board string representation from a board.
(defn board->str [board width]
  (join "\n" (map row->str (partition width board))))

;; x,y = position on the board where to put the piece.
;; p = piece, a value in the range 1..7.
;; pieceshape = the four dots [x y] that form the piece,
;;              e.g. [[0 0][1 0][2 0][1 1]] = T.
(defn- boardpiece [x y p board-width pieceshape]
  (vec (flatten (map (fn [[px py]] [(+ x px (* (+ y py) board-width)) p]) pieceshape))))

;; Sets a piece (p) on the board at position (x,y) with the rotation v.
(defn set-piece-on-board [board-width board x y p v]
  (def pieceshape ((:rotations (pieces p)) v))
  (apply assoc board (boardpiece x y p board-width pieceshape)))

;; Converts a list of board lines into a board
;; (convenient method to simplify the tests).
(defn !! [& str-board-rows] (str->board (vec str-board-rows)))
