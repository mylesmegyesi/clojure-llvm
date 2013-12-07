(ns clojure.llvm.main)

(defn parse-args [args]
  (loop [[current-arg next-arg & more] args results {}]
    (if (nil? current-arg)
      results
      (cond

        (and next-arg (= current-arg "-I"))
        (recur more (update-in results
                               [:search-paths]
                               #(conj (vec %) next-arg)))

        (and next-arg (= current-arg "-o"))
        (recur more (assoc results :output-file next-arg))

        (and next-arg (= current-arg "-m"))
        (recur more (assoc results :main next-arg))

        :else
        (recur (cons next-arg more)
               (update-in results
                          [:input-filenames]
                          #(conj (vec %) current-arg)))))))

(defn -main [& args]
  )
