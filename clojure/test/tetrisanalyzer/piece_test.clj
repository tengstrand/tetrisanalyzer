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
(expect \# (piece->char 9))

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
(expect 9 (char->piece \#))
