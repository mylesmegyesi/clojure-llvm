(ns clojure.llvm.main
  (:refer-clojure :exclude [compile])
  (:require [clojure.pprint :refer [pprint]]
            [clojure.llvm.compiler :refer [compile]]
            [clojure.java.io       :refer [file]]
            [llvm.core             :refer :all]
            ))

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

(def empty-env
  {:context :expr
   :locals {}
   :namespaces (atom {})})

(defn add-init-function [{:keys [context module builder] :as frame}]
  (let [void-ty       (get-void-ty-in-context context)
        function-type (function-type void-ty [] 0 false)
        func          (add-function module "__init__" function-type)
        basic-block   (append-basic-block-in-context context func "entry")
        func-frame    (assoc frame :init-function func)]
    (position-builder-at-end builder basic-block)
    (assoc frame :init-function func)))

(defn compile-input-files [{:keys [input-filenames]}]
  (for [input-filename input-filenames]
    (let [input-file    (file input-filename)
          absolute-path (.getCanonicalPath input-file)
          context       (get-global-context)]
      (binding [*file* absolute-path]
        (let [context (get-global-context)
              module (module-create-with-name-in-context (.getName input-file) context)]
          (compile (str "(do " (slurp input-filename) ")")
                   empty-env
                   (add-init-function {:context context
                                       :module module
                                       :builder (create-builder-in-context context)}))
          (dump-module module))))))

(defn -main [& args]
  (let [options (parse-args args)]
    (doall (compile-input-files options))))
