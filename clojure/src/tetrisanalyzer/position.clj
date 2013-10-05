(ns tetrisanalyzer.position
  (:require [tetrisanalyzer.piece :refer :all])
  (:require [tetrisanalyzer.board :refer :all]))

(defn all-piece-rotations
  [width]
  (vec (map #(piece->rotations % width) pieces)))

(defn all-piece-rotations
  [width]
  (vec (map #(piece->rotations % width) pieces)))

(defn new-position
  ([] (new-position (new-board)))
  ([board] {:board board
            :pieces (all-piece-rotations (:width board))}))

(defn set-piece [position piece rotation]
  (def piece-dots (get (get (:pieces position) piece) rotation))
  (def board (set-piece-on-board (:board position) piece-dots))
  (assoc position :board board))

;; ---------- Manual test ----------

;;(tetrisanalyzer.file/write-file! "C:/Source/IDEA/tetrisanalyzer/clojure/target/tetris.txt" (str newpos))
