(ns tetrisanalyzer.board)

(use '[clojure.string :only (join)])
(use '[tetrisanalyzer.piece])

(defn empty-dots
  ([] (empty-dots 6 5))
  ([width height]
  (vec (repeat (* width height) 0))))

;; Converts a row as string to a row.
(defn str->row [row-with-border]
  (def row (subs row-with-border 1 (dec (count row-with-border))))
  (vec (map #(char->piece %) row)))

;; Creates a new board.
(defn new-board
  ([] (new-board 6 5))
  ([rows]
   (def width (- (count (first rows)) 2))
   (def height (count rows))
   (def dots (vec (flatten (map #(str->row %) rows))))
   (new-board width height dots))
  ([width height] (new-board width height (empty-dots width height)))
  ([width height dots] {:width width :height height :dots dots}))

;; Converts a board row to a string representation, e.g. from [0 1 2 0] to "#-IZ-#".
(defn row->str [row]
  (str "#" (apply str (map #(piece->char %) row)) "#"))

;; Converts a board to a string representation.
(defn board->str [board]
  (def bottom (apply str (repeat (+ (board :width) 2) "#")))
  (str (join "\n" (map row->str (partition (board :width) (board :dots)))) "\n" bottom))

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
