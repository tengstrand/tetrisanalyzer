(ns tetrisanalyzer.move.move-test
  (:require [clojure.test :refer :all]
            [tetrisanalyzer.move.move :as move]
            [tetrisanalyzer.board.interface :as board]
            [tetrisanalyzer.piece.interface :as piece]
            [tetrisanalyzer.piece.settings.atari-arcade :as atari-arcade]))

(def x 2)
(def y 1)
(def rotation 0)
(def S piece/S)
(def shapes atari-arcade/shapes)
(def piece (piece/piece S rotation shapes))

(def board (board/board ['xxxxxxxx
                         'xxx--xxx
                         'xx--xxxx
                         'xxxxxxxx]))

(deftest valid-move
  (is (= true
         (move/valid-move? board x y S rotation shapes))))

(deftest invalid-move1
  (is (= false
         (move/valid-move? board (inc x) y S rotation shapes))))

(deftest invalid-move2
  (is (= false
         (move/valid-move? board x (dec y) S rotation shapes))))

(deftest valid-move3
  (is (= false
         (move/valid-move? board x y S (inc rotation) shapes))))

(deftest invalid-move2-outside-board
  (is (= false
         (move/valid-move? board 10 -10 S rotation shapes))))
