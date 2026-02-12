(ns tetrisanalyzer.board.interface
  (:require [tetrisanalyzer.board.core :as core]
            [tetrisanalyzer.board.grid :as grid]
            [tetrisanalyzer.board.clear-rows :as clear-rows]))

(defn width [board]
  (core/width board))

(defn height [board]
  (core/height board))

(defn board [grid-board]
  (grid/board grid-board))

(defn clear-rows [board]
  (clear-rows/clear-rows board))

(defn empty-board
  ([board]
   (core/empty-board (width board) (height board)))
  ([width height]
   (core/empty-board width height)))
