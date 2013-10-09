(ns tetrisanalyzer.repl
  (:require [tetrisanalyzer.piece :refer :all])
  (:require [tetrisanalyzer.board :refer :all])
  (:require [tetrisanalyzer.position :refer :all])
  (:require [tetrisanalyzer.file :refer :all]))



(board->str (empty-board) 8)


(defn position->str [position] (board->str (:board position) (:board-width position)))

;;(board->str (:board pos) (:board-width pos))

(def pos {:board (empty-board) :board-width 8 :piece 1 :piece-pos {:x 0 :y 0 :v 0}})


;; board-width board x y p v
(def board  (set-piece-on-board 8 (empty-board) 2 2 6 1))

board

(write-board! board)

;; (reduce (fn [a b] (+ a b)) [1 2 3])


