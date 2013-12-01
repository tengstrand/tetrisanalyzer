(ns tetrisanalyzer.core-test
  (:require [expectations :refer :all])
  (:require [tetrisanalyzer.core :refer :all]))

;; ===== Piece =====

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

;; ===== Board =====

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

(expect { [0 0] 9, [0 1] 0, [0 2] 6, [0 3] 0, [0 4] 0, [0 5] 9,
          [1 0] 9, [1 1] 6, [1 2] 6, [1 3] 0, [1 4] 2, [1 5] 9,
          [2 0] 9, [2 1] 9, [2 2] 9, [2 3] 9, [2 4] 9, [2 5] 9 }
         (new-board ["#-T--#"
                     "#TT-Z#"
                     "######"]))

;; ===== Move =====

;; This function returns a list of "pairs": [y x] piece
;; that can be used by the function assoc
;; (via set-piece) to put a piece on a board.
;;
;;                         piece rotation x y
;;                         ----- -------- - -
;;  (rotate-and-move-piece    6      1    4 2)
;;
;;        123456           (6 = T)
;;    0  #------#
;;    1  #------#
;;    2  #---T--#   [2 4]
;;    3  #---TT-#   [3 4] [3 5]
;;    4  #---T--#   [4 4]
;;       ########
(expect '([2 4] [3 4] [3 5] [4 4])
        (rotate-and-move-piece 6 1 4 2))

;; Returns a new board with a piece set, e.g.:
;;
;;              board       piece piece-shape
;;              ----------- ----- -----------
;;   (set-piece empty-board    2    z-piece
;;
;;    piece = 2      piece Z
;;    piece-shape    the four squares that form the Z piece
;;    rotation = 0   no rotation
;;    x,y = 3,1      position on the board
;;
(def z-piece (rotate-and-move-piece 2 0 3 1))
(def z-board (set-piece empty-board 2 z-piece))
(expect (str "#------#\n"
             "#--ZZ--#\n"
             "#---ZZ-#\n"
             "#------#\n"
             "#------#\n"
             "########")
        (board->str z-board 8))

(expect true (piece-occupied? z-board 2 {:rotation 0, :x 2, :y 1}))
(expect false (piece-occupied? z-board 2 {:rotation 0, :x 1, :y 1}))

(def s-board (new-board ["#------#"
                         "#------#"
                         "#------#"
                         "#------#"
                         "#----S-#"
                         "########"]))

(expect #{{:rotation 0, :x 1, :y 3}
          {:rotation 0, :x 2, :y 3}
          {:rotation 0, :x 3, :y 2}
          {:rotation 0, :x 4, :y 2}
          {:rotation 1, :x 1, :y 2}
          {:rotation 1, :x 2, :y 2}
          {:rotation 1, :x 3, :y 2}
          {:rotation 1, :x 4, :y 2}
          {:rotation 1, :x 5, :y 1}}
  (valid-moves s-board 2 1 {:rotation 0, :x 3, :y 0} #{} #{}))

