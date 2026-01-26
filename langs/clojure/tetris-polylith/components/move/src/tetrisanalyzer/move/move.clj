(ns tetrisanalyzer.move.move
  (:require [tetrisanalyzer.piece.interface :as piece]))

(defn cell [board x y [cx cy]]
  (or (get-in board [(+ y cy) (+ x cx)])
      piece/X))

(defn valid-move? [board x y p rotation shapes]
  (every? zero?
          (map #(cell board x y %)
               (piece/piece p rotation shapes))))

(defn left [board x y p rotation _ shapes]
  (when (valid-move? board (dec x) y p rotation shapes)
    [(dec x) y rotation]))

(defn right [board x y p rotation _ shapes]
  (when (valid-move? board (inc x) y p rotation shapes)
    [(inc x) y rotation]))

(defn down
  "Returns [down-move placement] where:
   - down-move: next move when moving down or nil if blocked
   - placement: final placement if blocked, or nil if can move down"
  [board x y p rotation _ shapes]
  (if (valid-move? board x (inc y) p rotation shapes)
    [[x (inc y) rotation] nil]
    [nil [[x y rotation]]]))

(defn rotate [board x y p rotation bitmask shapes]
  (let [new-rotation (bit-and (inc rotation) bitmask)]
    (when (valid-move? board x y p new-rotation shapes)
      [x y new-rotation])))

(defn rotate-with-kick [board x y p rotation bitmask shapes]
  (or (rotate board x y p rotation bitmask shapes)
      (rotate board (dec x) y p rotation bitmask shapes)))

(defn rotation-fn [rotation-kick?]
  (if rotation-kick?
    rotate-with-kick
    rotate))
