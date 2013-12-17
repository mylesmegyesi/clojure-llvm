(ns llvm.core
  (:require [llvm.library :refer [llvm-library]])
  (:import  [jnr.ffi Pointer]))

(defn create-builder-in-context [context]
  (.LLVMCreateBuilderInContext @llvm-library context))

(defn get-global-context []
  (.LLVMGetGlobalContext @llvm-library))

(defn module-create-with-name-in-context [name context]
  (.LLVMModuleCreateWithNameInContext @llvm-library name context))

(defn get-int-32-ty-in-context [context]
  (.LLVMInt32TypeInContext @llvm-library context))

(defn get-void-ty-in-context [context]
  (.LLVMVoidTypeInContext @llvm-library context))

(defn function-type [return-type param-types param-count variadic-arguments?]
  (.LLVMFunctionType @llvm-library
                     return-type
                     (into-array Pointer param-types)
                     param-count
                     variadic-arguments?))

(defn add-function [module name function-type]
  (.LLVMAddFunction @llvm-library module name function-type))

(defn append-basic-block-in-context [context function name]
  (.LLVMAppendBasicBlockInContext @llvm-library context function name))

(defn position-builder-at-end [builder basic-block]
  (.LLVMPositionBuilderAtEnd @llvm-library builder basic-block))

(defn const-int [int-ty value sign-extend?]
  (.LLVMConstInt @llvm-library int-ty value sign-extend?))

(defn const-add [lhs-constant rhs-constant]
  (.LLVMConstAdd @llvm-library lhs-constant rhs-constant))

(defn build-ret [builder value]
  (.LLVMBuildRet @llvm-library builder value))

(defn dump-module [module]
  (.LLVMDumpModule @llvm-library module))

(defn build-cond-br [builder if-value then-bb else-bb]
  (.LLVMBuildCondBr @llvm-library builder if-value then-bb else-bb))

(defn build-br [builder dest]
  (.LLVMBuildBr @llvm-library builder dest))

(defn get-insert-block [builder]
  (.LLVMGetInsertBlock @llvm-library builder))

(defn build-phi [builder type name]
  (.LLVMBuildPhi @llvm-library builder type name))

(defn add-incoming [phi-node incoming-values incoming-blocks count]
  (.LLVMAddIncoming @llvm-library
                    phi-node
                    (into-array Pointer incoming-values)
                    (into-array Pointer incoming-blocks)
                    count))

(defn build-call [builder fn args num-args name]
  (.LLVMBuildCall @llvm-library builder fn (into-array Pointer args) num-args name))

(defn const-i-cmp [predicate lhs rhs]
  (.LLVMConstICmp @llvm-library predicate lhs rhs))
