(ns tetrisanalyzer.move.placement-test
  (:require [clojure.test :refer :all]
            [tetrisanalyzer.piece.interface :as piece]
            [tetrisanalyzer.move.placement :as valid-moves]
            [tetrisanalyzer.piece.settings.atari-arcade :as atari-arcade]))

(def start-x 2)
(def sorter (juxt second first last))

(def board [[0 0 0 0 0 0]
            [0 0 1 1 0 0]
            [0 0 1 0 0 1]
            [0 0 1 1 1 1]])

(def shapes atari-arcade/shapes)

;; Start position of the J piece:
;; --JJJ-
;; --xxJ-
;; --x--x
;; --xxxx
(deftest placements--without-rotation-kick
  (is (= [[2 0 0]
          [3 0 0]]
         (sort-by sorter (valid-moves/placements board piece/J start-x false shapes)))))

;; With rotation kick, checking if x-1 fits:
;; -JJ---
;; -Jxx--
;; -Jx--x
;; --xxxx
(deftest placements--with-rotation-kick
  (is (= [[1 0 1]
          [2 0 0]
          [3 0 0]
          [0 1 1]]
         (sort-by sorter (valid-moves/placements board piece/J start-x true shapes)))))
