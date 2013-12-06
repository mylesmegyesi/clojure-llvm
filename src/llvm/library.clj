(ns llvm.library
  (:require [clojure.java.shell :refer [sh]]
            [clojure.string     :refer [trim]])
  (:import jnr.ffi.LibraryLoader llvm.LLVMWrapper))

(def llvm-library
  (let [libdir (-> (sh "llvm-config" "--libdir") :out trim)]
    (delay
      (-> llvm.LLVMWrapper
        LibraryLoader/create
        (.search libdir)
        (.load "LLVM-3.2")))))
