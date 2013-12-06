(ns llvm.test-main
  (:require [llvm.library :refer [llvm-library]]))

(defn -main [& args]
  (let [context-ref (.LLVMGetGlobalContext @llvm-library)
        module-ref (.LLVMModuleCreateWithNameInContext @llvm-library "my cool module" context-ref)
        builder-ref (.LLVMCreateBuilderInContext @llvm-library context-ref)
        int32-type (.LLVMInt32TypeInContext @llvm-library context-ref)
        function-type (.LLVMFunctionType @llvm-library int32-type (into-array jnr.ffi.Pointer []) 0 false)
        func (.LLVMAddFunction @llvm-library module-ref "" function-type)
        basic-block (.LLVMAppendBasicBlockInContext @llvm-library context-ref func "entry")
        _ (.LLVMPositionBuilderAtEnd @llvm-library builder-ref basic-block)
        one (.LLVMConstInt @llvm-library int32-type 1 false)
        two (.LLVMConstInt @llvm-library int32-type 2 false)
        one-plus-two (.LLVMConstAdd @llvm-library one two)
        ret (.LLVMBuildRet @llvm-library builder-ref one-plus-two)]
    (.LLVMDumpModule @llvm-library module-ref)))
