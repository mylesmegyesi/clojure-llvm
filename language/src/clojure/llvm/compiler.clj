(ns clojure.llvm.compiler
  (:refer-clojure :exclude [read-string compile])
  (:require [clojure.tools.reader.edn :refer [read-string]]
            [clojure.llvm.analyzer    :refer [analyze]]
            [clojure.llvm.emitter     :refer [emit]]))

(defn compile [string env]
  (-> string
    read-string
    (analyze env)
    emit))
