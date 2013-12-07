package llvm;

import jnr.ffi.Pointer;

public interface LLVMWrapper {

  public Pointer LLVMGetGlobalContext();

  public Pointer LLVMModuleCreateWithNameInContext(String s, Pointer p);

  public Pointer LLVMCreateBuilderInContext(Pointer p);

  public Pointer LLVMFunctionType(Pointer ReturnType, Pointer[] ParamTypes, int ParamCount, boolean isVarArg);

  public Pointer LLVMInt32TypeInContext(Pointer p);

  public Pointer LLVMAppendBasicBlockInContext(Pointer C, Pointer Fn, String Name);

  public Pointer LLVMAddFunction(Pointer M, String Name, Pointer FunctionTy);

  public void LLVMPositionBuilderAtEnd(Pointer Builder, Pointer Block);

  public Pointer LLVMConstInt(Pointer IntTy, long N, boolean SignExtend);

  public Pointer LLVMConstAdd(Pointer LHSConstant, Pointer RHSConstant);

  public Pointer LLVMBuildRet(Pointer LLVMBuilderRef, Pointer V);

  public void LLVMDumpModule(Pointer M);

  public String LLVMPrintModuleToString(Pointer M);

  public String LLVMPrintValueToString(Pointer Val);

}
