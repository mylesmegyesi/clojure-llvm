package llvm;

import jnr.ffi.Pointer;

public interface LLVMWrapper {

  public Pointer LLVMGetGlobalContext();

  public Pointer LLVMModuleCreateWithNameInContext(String s, Pointer p);

  public Pointer LLVMCreateBuilderInContext(Pointer p);

  public Pointer LLVMFunctionType(Pointer ReturnType, Pointer[] ParamTypes, int ParamCount, boolean isVarArg);

  public Pointer LLVMInt32TypeInContext(Pointer p);

  public Pointer LLVMVoidTypeInContext(Pointer p);

  public Pointer LLVMAppendBasicBlockInContext(Pointer C, Pointer Fn, String Name);

  public Pointer LLVMAddFunction(Pointer M, String Name, Pointer FunctionTy);

  public void LLVMPositionBuilderAtEnd(Pointer Builder, Pointer Block);

  public Pointer LLVMConstInt(Pointer IntTy, long N, boolean SignExtend);

  public Pointer LLVMConstAdd(Pointer LHSConstant, Pointer RHSConstant);

  public Pointer LLVMBuildRet(Pointer LLVMBuilderRef, Pointer V);

  public void LLVMDumpModule(Pointer M);

  public String LLVMPrintModuleToString(Pointer M);

  public String LLVMPrintValueToString(Pointer Val);

  public Pointer LLVMBuildCondBr(Pointer builder, Pointer If, Pointer Then, Pointer Else);

  public Pointer LLVMBuildBr(Pointer builder, Pointer Dest);

  public Pointer LLVMGetInsertBlock(Pointer Builder);

  public Pointer LLVMBuildPhi(Pointer Builder, Pointer Ty, String Name);

  public void LLVMAddIncoming(Pointer PhiNode, Pointer[] IncomingValues, Pointer[] IncomingBlocks, long Count);

  public Pointer LLVMBuildCall(Pointer builder, Pointer Fn, Pointer[] Args, long NumArgs, String Name);

  public Pointer LLVMConstICmp(int Predicate, Pointer LHSConstant, Pointer RHSConstant);

}
