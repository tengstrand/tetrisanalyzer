(ns tetrisanalyzer.board.core-test
  (:require [clojure.test :refer :all]
            [tetrisanalyzer.board.core :as board]
            [tetrisanalyzer.piece.interface :as piece]
            [tetrisanalyzer.piece.settings.atari-arcade :as atari-arcade]))

(def empty-board [[0 0 0 0 0 0 0 0 0 0]
                  [0 0 0 0 0 0 0 0 0 0]
                  [0 0 0 0 0 0 0 0 0 0]
                  [0 0 0 0 0 0 0 0 0 0]
                  [0 0 0 0 0 0 0 0 0 0]
                  [0 0 0 0 0 0 0 0 0 0]
                  [0 0 0 0 0 0 0 0 0 0]
                  [0 0 0 0 0 0 0 0 0 0]
                  [0 0 0 0 0 0 0 0 0 0]
                  [0 0 0 0 0 0 0 0 0 0]
                  [0 0 0 0 0 0 0 0 0 0]
                  [0 0 0 0 0 0 0 0 0 0]
                  [0 0 0 0 0 0 0 0 0 0]
                  [0 0 0 0 0 0 0 0 0 0]
                  [0 0 0 0 0 0 0 0 0 0]])

(deftest empty-board-test
  (is (= empty-board
         (board/empty-board 10 15))))

(deftest set-piece-test
  (let [T piece/T
        rotate-two-times 2
        shapes atari-arcade/shapes
        piece-t (piece/piece T rotate-two-times shapes)
        x 5
        y 13]
    (is (= [[0 0 0 0 0 0 0 0 0 0]
            [0 0 0 0 0 0 0 0 0 0]
            [0 0 0 0 0 0 0 0 0 0]
            [0 0 0 0 0 0 0 0 0 0]
            [0 0 0 0 0 0 0 0 0 0]
            [0 0 0 0 0 0 0 0 0 0]
            [0 0 0 0 0 0 0 0 0 0]
            [0 0 0 0 0 0 0 0 0 0]
            [0 0 0 0 0 0 0 0 0 0]
            [0 0 0 0 0 0 0 0 0 0]
            [0 0 0 0 0 0 0 0 0 0]
            [0 0 0 0 0 0 0 0 0 0]
            [0 0 0 0 0 0 0 0 0 0]
            [0 0 0 0 0 0 T 0 0 0]
            [0 0 0 0 0 T T T 0 0]]
           (board/set-piece empty-board T x y piece-t)))))
