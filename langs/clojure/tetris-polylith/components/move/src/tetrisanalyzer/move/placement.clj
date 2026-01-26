(ns tetrisanalyzer.move.placement
  (:require [tetrisanalyzer.move.move :as move]
            [tetrisanalyzer.move.visit :as visit]
            [tetrisanalyzer.board.interface :as board]
            [tetrisanalyzer.piece.interface :as piece]))

(defn placements
  [board x y p rotation bitmask valid-moves visited-moves rotation-fn shapes]
  (loop [next-moves (list [x y rotation])
         placements []
         valid-moves valid-moves
         visited-moves visited-moves]
    (if-let [[x y rotation] (first next-moves)]
      (let [next-moves (rest next-moves)]
        (if (visit/visited? visited-moves x y rotation)
          (recur next-moves placements valid-moves visited-moves)
          (let [[down placement] (move/down board x y p rotation bitmask shapes)
                moves (keep #(% board x y p rotation bitmask shapes)
                            [move/left
                             move/right
                             rotation-fn
                             (constantly down)])]
            (recur (into next-moves moves)
                   (concat placements placement)
                   (conj valid-moves [x y rotation])
                   (visit/visit visited-moves x y rotation)))))
      placements)))

(defn all-placements [board p start-x kick? shapes]
  (let [bitmask (piece/rotation-bitmask shapes p)
        visited-moves (board/empty-board board)
        rotation-fn (move/rotation-fn kick?)]
    (if (move/valid-move? board start-x 0 p 0 shapes)
      (placements board start-x 0 p 0 bitmask [] visited-moves rotation-fn shapes)
      [])))
