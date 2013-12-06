(ns llvm.library
  (:import jnr.ffi.LibraryLoader llvm.LLVMWrapper))

(def llvm-library
  (delay
    (-> llvm.LLVMWrapper
      LibraryLoader/create
      (.search "llvm")
      (.load "LLVM-3.2"))))
