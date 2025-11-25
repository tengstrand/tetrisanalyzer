(ns tetrisanalyzer.piece.interface
  (:require [clojure.set :as set]
            [tetrisanalyzer.piece.piece :as piece]))

(def I 1)
(def Z 2)
(def S 3)
(def J 4)
(def L 5)
(def T 6)
(def O 7)

(def char->piece-index
  {\- 0, \I 1, \Z 2, \S 3, \J 4, \L 5, \T 6, \O 7})

(def piece-index->char
  (set/map-invert char->piece-index))

(defn piece [p rotation]
  (get-in piece/pieces [p rotation]))
