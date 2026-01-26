(ns tetrisanalyzer.piece.interface
  (:require [tetrisanalyzer.piece.piece :as piece]
            [tetrisanalyzer.piece.bitmask :as bitmask]))

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

(defn rotation-bitmask [shapes p]
  (bitmask/rotation-bitmask shapes p))
