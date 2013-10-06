(ns tetrisanalyzer.board)

(use '[clojure.string :only (join)])
(use '[tetrisanalyzer.piece])

(defn empty-dots
  ([] (empty-dots 6 5))
  ([width height]
  (vec (repeat (* width height) 0))))

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

;; Converts a row string representation to a board row.
(defn str->row [row]
  (vec (map #(char->piece %) row)))

;; Creates a new board.
(defn new-board
  ([] (new-board 6 5))
  ([rows]
   (def width (- (count (first rows)) 2))
   (def height (dec (count rows)))
   (def rows-skip-bottom (subvec rows 0 height))
   (def dots (vec (flatten (map #(str->row %) rows-skip-bottom))))
   (new-board width height dots))
  ([width height] (new-board width height (empty-dots width height)))
  ([width height dots] {:width width :height height :dots dots}))

(defn str->board
  ([rows]
    (vec (flatten (map #(str->row %) rows)))))

;; Converts a board row to a string representation, e.g. from [0 1 2 0] to "#-IZ-#".
(defn row->str [row]
  (str "#" (apply str (map #(piece->char %) row)) "#"))

(defn row->str2 [row]
  (apply str (map #(piece->char %) row)))

;; Converts a board to a string representation.
(defn board->str [board]
  (def bottom (apply str (repeat (+ (board :width) 2) "#")))
  (str (join "\n" (map row->str (partition (board :width) (board :dots)))) "\n" bottom))

(defn board->str2 [board width]
  (join "\n" (map row->str2 (partition width board))))

;; Converts four piece dots into a vector that can be used to put a piece
;; on a board (with a given width) using the 'assoc' function.
(defn dots->piece
  [dots board-width p]
  (vec (flatten (map (fn [[x y]] (vector (+ x (* y board-width)) p)) dots))))

;; Converts from a piece to indexes on a board.
(defn piece->rotations [piece board-width]
  (vec (map #(dots->piece % board-width (:index piece)) (:rotations piece))))

;; Places a piece on a board, currently in position (0,0).
(defn set-piece-on-board [board boardpiece]
  (def dots (apply assoc (:dots board) boardpiece))
  (assoc board :dots dots))

(defn boardpiece [x y p board-width pieceshape]
  (vec (flatten (map (fn [[x1 y1]] [(+ x x1 (* (+ y y1) board-width)) p]) pieceshape))))

;; used by the tests
(defn !! [& board] (new-board (vec board)))
(defn !!! [& board] (str->board (vec board)))

;; Sets a piece (p) on the board at position (x,y) with rotation v.
(defn set-piece-on-board2 [board board-width x y p v]
  (def piece ((:rotations (pieces p)) v))
  (apply assoc board (boardpiece x y p board-width piece)))

