(ns clojure.core-test)

(defn something []

  (assert (= 2 (+ 1 1)))

  )

(defn -main [& args]
  (something))
