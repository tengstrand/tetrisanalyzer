(ns tetrisanalyzer.move.visit-test
  (:require [clojure.test :refer :all]
            [tetrisanalyzer.move.visit :as visit]))

(def x 2)
(def y 1)
(def rotation 3)
(def unvisited [[0 0 0 0]
                [0 0 0 0]])

(deftest move-is-not-visited
  (is (= false
         (visit/visited? unvisited x y rotation))))

(deftest move-is-visited
  (let [x 2
        y 1
        rotation 0
        visited (visit/visit unvisited x y rotation)]
    (is (= true
           (visit/visited? visited x y rotation)))))
