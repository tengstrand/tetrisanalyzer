(ns tetrisanalyzer.piece.interface
  (:require [tetrisanalyzer.piece.shape :as shape]))

(def I 1)
(def Z 2)
(def S 3)
(def J 4)
(def L 5)
(def T 6)
(def O 7)

(defn piece [p rotation]
  (get-in shape/pieces [p rotation]))
