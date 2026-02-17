(ns tetrisanalyzer.piece.interface
  (:require [tetrisanalyzer.piece.piece :as piece]
            [tetrisanalyzer.piece.move.placement :as placement]))

(defn set-piece [board p x y piece]
  (piece/set-piece board p x y piece))

(defn placements [board p x kick? shapes]
  (placement/placements board p x kick? shapes))
