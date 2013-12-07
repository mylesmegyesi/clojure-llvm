# compiler-llvm

An experiment to compile Clojure using the LLVM.

```bash
$ cd compiler
$ lein run -- -m "clojure.core-test" -o "out" ../stdlib/test/clojure/core_test.clj
$ ./out
```
