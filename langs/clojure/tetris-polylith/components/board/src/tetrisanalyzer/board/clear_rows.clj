(ns tetrisanalyzer.board.clear-rows
  (:require [tetrisanalyzer.board.core :as core]))

(defn incomplete-row? [row]
  (some zero? row))

(defn clear-rows [board]
  (let [width (count (first board))
        height (count board)
        remaining-rows (filter incomplete-row? board)
        num-cleared-rows (- height (count remaining-rows))
        empty-rows (core/empty-board width num-cleared-rows)]
    (vec (concat empty-rows remaining-rows))))
