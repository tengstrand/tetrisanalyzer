(ns tetrisanalyzer.core-test
  (:require [expectations :refer :all])
  (:require [tetrisanalyzer.core :refer :all]))

(defn write-file! [content]
  (with-open [w (clojure.java.io/writer "C:/Clojure/tetris.txt")]
    (.write w content)))

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

;; Default board size is 10x20.
(expect (str (apply str (repeat 20 "#----------#\n")) "############")
        (board->str (new-board) 12))

(def empty-board (new-board 8 6))

(expect (str "#------#\n"
             "#------#\n"
             "#------#\n"
             "#------#\n"
             "#------#\n"
             "########")
        (board->str empty-board 8))

(expect (new-board ["#-T--#"
                    "#TT-Z#"
                    "######"])
        '([[0 0] 9] [[0 1] 0] [[0 2] 6] [[0 3] 0] [[0 4] 0] [[0 5] 9]
          [[1 0] 9] [[1 1] 6] [[1 2] 6] [[1 3] 0] [[1 4] 2] [[1 5] 9]
          [[2 0] 9] [[2 1] 9] [[2 2] 9] [[2 3] 9] [[2 4] 9] [[2 5] 9]))

;; move-piece returns a list of pairs: [y x] p
;; that can be used by the function assoc
;; (via set-piece) to put a piece on a board.
;;
;;              p v x y
;;              - - - -
;;  (move-piece 6 1 4 2)
;;
;;        123456
;;    0  #------#   (6 = T)
;;    1  #------#
;;    2  #---T--#   [2 4] 6
;;    3  #---TT-#   [3 4] 6 [3 5] 6
;;    4  #---T--#   [4 4] 6
;;       ########
(expect '([2 4] 6 [3 4] 6 [3 5] 6 [4 4] 6)
        (move-piece 6 1 4 2))

;; Returns a new board with a piece set, e.g.:
;;
;;              board       p v x y
;;              ----------- - - - -
;;   (set-piece empty-board 2 0 3 1)
;;
;;    p = 2      piece Z
;;    v = 0      no rotation
;;    x,y = 3,1  position on the board
;;
(expect (str "#------#\n"
             "#---ZZ-#\n"
             "#--ZZ--#\n"
             "#------#\n"
             "#------#\n"
             "########")
        (board->str (set-piece empty-board 2 0 3 1) 8))
