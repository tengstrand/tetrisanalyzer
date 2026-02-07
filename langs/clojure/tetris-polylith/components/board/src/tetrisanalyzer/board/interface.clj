(ns tetrisanalyzer.board.interface
  (:require [tetrisanalyzer.board.core :as core]))

(defn width [board]
  (core/width board))

(defn height [board]
  (core/height board))

(defn empty-board
  ([board]
   (core/empty-board (width board) (height board)))
  ([width height]
   (core/empty-board width height)))

(defn set-piece [board p x y piece]
  (core/set-piece board p x y piece))
