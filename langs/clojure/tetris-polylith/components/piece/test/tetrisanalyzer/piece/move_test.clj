(ns tetrisanalyzer.piece.move-test
  (:require [clojure.test :refer :all]
            [tetrisanalyzer.piece.piece :as piece]
            [tetrisanalyzer.piece.move :as move]
            [tetrisanalyzer.piece.bitmask :as bitmask]
            [tetrisanalyzer.board.interface :as board]
            [tetrisanalyzer.piece.settings.atari-arcade :as atari-arcade]))

(def x 2)
(def y 1)
(def rotation 0)
(def S piece/S)
(def shapes atari-arcade/shapes)
(def bitmask (bitmask/rotation-bitmask shapes S))
(def piece (piece/piece S rotation shapes))

(def board (board/board ['xxxxxxxx
                         'xxx--xxx
                         'xx--xxxx
                         'xxxxxxxx]))

(deftest valid-move
  (is (= true
         (move/valid-move? board x y S rotation shapes))))

(deftest valid-left-move
  (is (= [2 1 0]
         (move/left board (inc x) y S rotation nil shapes))))

(deftest invalid-left-move
  (is (= nil
         (move/left board x y S rotation nil shapes))))

(deftest valid-right-move
  (is (= [2 1 0]
         (move/right board (dec x) y S rotation nil shapes))))

(deftest invalid-right-move
  (is (= nil
         (move/right board x (dec y) S rotation nil shapes))))

(deftest unoccupied-down-move
  (is (= [[2 1 0] nil]
         (move/down board x (dec y) S rotation nil shapes))))

(deftest down-move-hits-ground
  (is (= [nil [[2 1 0]]]
         (move/down board x y S rotation nil shapes))))

(deftest valid-rotation
  (is (= [2 1 0]
         (move/rotate board x y S (dec rotation) bitmask shapes))))

(deftest invalid-rotation-without-kick
  (is (= nil
         (move/rotate board (inc x) y S (inc rotation) bitmask shapes))))

(deftest valid-rotation-with-kick
  (is (= [2 1 0]
         (move/rotate-with-kick board (inc x) y S (inc rotation) bitmask shapes))))

(deftest invalid-move-outside-board
  (is (= false
         (move/valid-move? board 10 -10 S rotation shapes))))
