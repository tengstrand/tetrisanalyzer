(ns tetrisanalyzer.file)

(defn write-file! [filename string]
  (with-open [w (clojure.java.io/writer filename :append true)]
    (.write w string)))
