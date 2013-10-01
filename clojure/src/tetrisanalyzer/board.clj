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

;; Creates an empty board.
(defn empty-board
  ([] (empty-board 6 5))
  ([rows]
   (def width (- (count (first rows)) 2))
   (def height (count rows))
   (def dots (vec (flatten (map #(str->row %) rows))))
   (empty-board width height dots))
  ([width height] (empty-board width height (empty-dots width height)))
  ([width height dots] {:width width :height height :dots dots
                        :pieces (vec (map #(piece->rotations % width) pieces))}))

;; Converts a board row to a string representation, e.g. from [0 1 2 0] to "#-IZ-#".
(defn row->str [row]
  (str "#" (apply str (map #(piece->char %) row)) "#"))

;; Converts a board to a string representation.
(defn board->str [board]
  (def bottom (apply str (repeat (+ (board :width) 2) "#")))
  (str (join "\n" (map row->str (partition (board :width) (board :dots)))) "\n" bottom))

;; Places a piece on a board with a given rotation, currently in position (0,0).
(defn set-piece [board piece rotation]
  (def piece-dots (get (get (:pieces board) piece) rotation))
  (def board-dots (apply assoc (board :dots) piece-dots))
    (assoc board :dots board-dots))
