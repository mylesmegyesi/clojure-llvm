(ns clojure.llvm.interpreter
  (:refer-clojure :exclude [eval compile])
  (:require [clojure.llvm.compiler :refer [compile]]))

(defn execute [llvm-ir]
  llvm-ir)

(defn runtime-env [current-ns]
  {:context :expr
   :locals {}
   :ns (ns-name current-ns)
   :namespaces (atom
                 (into {} (mapv #(vector (ns-name %)
                                         {:mappings (ns-map %)
                                          :aliases  (reduce-kv (fn [a k v] (assoc a k (ns-name v)))
                                                               {} (ns-aliases %))
                                          :ns       (ns-name %)})
                                (all-ns))))})

(defn -eval [string env]
  (-> string
    (compile env)
    execute))

(defmacro eval
  ([string]
   `(-eval ~string (runtime-env *ns*)))
  ([string env]
   `(-eval ~string ~env)))
