(ns clojure.llvm.compiler
  (:refer-clojure :exclude [eval read-string compile])
  (:require [clojure.tools.reader.edn :refer [read-string]]
            [clojure.llvm.analyzer    :refer [analyze]]
            [clojure.llvm.emitter     :refer [emit]]
            [clojure.llvm.interpreter :refer [execute]]))

(defn compile [string env]
  (-> string
    read-string
    (analyze env)
    emit))

(defn- runtime-env []
  {:context :expr
   :locals {}
   :ns (ns-name *ns*)
   :namespaces (atom
                 (into {} (mapv #(vector (ns-name %)
                                         {:mappings (ns-map %)
                                          :aliases  (reduce-kv (fn [a k v] (assoc a k (ns-name v)))
                                                               {} (ns-aliases %))
                                          :ns       (ns-name %)})
                                (all-ns))))})

(defn eval
  ([string]
    (eval string (runtime-env)))
  ([string env]
    (-> string
      (compile env)
      execute)))
