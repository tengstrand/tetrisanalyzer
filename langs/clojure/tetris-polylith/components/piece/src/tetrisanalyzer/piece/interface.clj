(ns tetrisanalyzer.piece.interface
  (:require [tetrisanalyzer.piece.piece :as piece]
            [tetrisanalyzer.piece.placement.placement :as placement]))

(def O piece/O)
(def I piece/I)
(def Z piece/Z)
(def S piece/S)
(def J piece/J)
(def L piece/L)
(def T piece/T)
(def X piece/X) ;; Outside the board

(defn piece [p rotation shapes]
  (piece/piece p rotation shapes))

(defn set-piece [board p x y piece]
  (piece/set-piece board p x y piece))

(defn placements [board p x kick? shapes]
  (placement/placements board p x kick? shapes))
