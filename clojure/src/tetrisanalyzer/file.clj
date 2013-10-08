(ns tetrisanalyzer.file
  (:require [tetrisanalyzer.board :refer :all]))

(defn write-file! [filename string]
  (with-open [w (clojure.java.io/writer filename)]
    (.write w string)))

(defn write-board!
  ([board] (write-board! board 8))
  ([board width]
  (write-file! "C:/TetrisAnalyzer/tetris.txt"
    (str "\n\n" (board->str board width)))))

