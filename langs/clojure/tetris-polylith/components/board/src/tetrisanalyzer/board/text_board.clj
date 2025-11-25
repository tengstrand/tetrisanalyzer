(ns tetrisanalyzer.board.text-board
  (:require [clojure.string :as str]
            [tetrisanalyzer.piece.interface :as piece]))

(defn text-row->board-line [text-line]
  (mapv piece/char->piece-index text-line))

(defn text-board->board [text-board]
  (mapv text-row->board-line text-board))

(defn board-row->text [board-row]
  (str/join "" (map piece/piece-index->char board-row)))

(defn board->text-board [board]
  (mapv board-row->text board))
