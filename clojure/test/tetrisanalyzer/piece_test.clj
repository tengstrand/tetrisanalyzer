(ns tetrisanalyzer.piece-test
  (:require [expectations :refer :all]
            [tetrisanalyzer.piece :refer :all]))

;; Convert from piece index to character.
(expect \- (piece->char 0))
(expect \I (piece->char 1))
(expect \Z (piece->char 2))
(expect \S (piece->char 3))
(expect \J (piece->char 4))
(expect \L (piece->char 5))
(expect \T (piece->char 6))
(expect \O (piece->char 7))
(expect \x (piece->char 8))

;; Convert from character to piece index.
(expect 0 (char->piece \-))
(expect 1 (char->piece \I))
(expect 2 (char->piece \Z))
(expect 3 (char->piece \S))
(expect 4 (char->piece \J))
(expect 5 (char->piece \L))
(expect 6 (char->piece \T))
(expect 7 (char->piece \O))
(expect 8 (char->piece \x))

;; Should create a vector used to place a piece (7) on a board with width 4.
(expect [0 7 1 7 2 7 5 7] (dots->piece [[0 0][1 0][2 0][1 1]] 4 7))

;; Should create a vector used to place the the different shapes of piece Z on a board with witdh 4.
(expect [[0 2 1 2 5 2 6 2][1 2 4 2 5 2 8 2]]
        (piece->rotations Z 4))

