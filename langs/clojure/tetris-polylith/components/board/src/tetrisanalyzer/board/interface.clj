(ns tetrisanalyzer.board.interface
  (:require [tetrisanalyzer.board.core :as core]
            [tetrisanalyzer.board.grid :as grid]))

(defn width [board]
  (core/width board))

(defn height [board]
  (core/height board))

(defn board [grid-board]
  (grid/board grid-board))

(defn empty-board
  ([board]
   (core/empty-board (width board) (height board)))
  ([width height]
   (core/empty-board width height)))
