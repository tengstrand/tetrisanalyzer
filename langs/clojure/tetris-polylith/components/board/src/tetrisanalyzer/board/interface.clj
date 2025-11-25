(ns tetrisanalyzer.board.interface
  (:require [tetrisanalyzer.board.board :as board]
            [tetrisanalyzer.board.text-board :as text-board]))

(defn empty-board [width height]
  (board/empty-board width height))

(defn text-board->board [text-board]
  (text-board/text-board->board text-board))

(defn board->text-board [board]
  (text-board/board->text-board board))
