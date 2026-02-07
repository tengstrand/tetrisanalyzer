(ns tetrisanalyzer.move.interface
  (:require [tetrisanalyzer.move.placement :as placement]))

(defn placements [board p x kick? shapes]
  (placement/placements board p x kick? shapes))
