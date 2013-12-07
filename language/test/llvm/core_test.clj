(ns llvm.core-test
  (:require [clojure.test :refer :all]
            [llvm.core    :refer :all]))

(deftest llvm.core

  ;(testing "one plus one"
  ;  (let [context       (get-global-context)
  ;        module        (module-create-with-name-in-context "my cool module" context)
  ;        builder       (create-builder-in-context context)
  ;        int32-type    (get-int-32-ty context)
  ;        function-type (function-type int32-type [] 0 false)
  ;        func          (add-function module "" function-type)
  ;        basic-block   (append-basic-block-in-context context func "entry")
  ;        _             (position-builder-at-end builder basic-block)
  ;        one           (const-int int32-type 1 false)
  ;        one-plus-one  (const-add one one)
  ;        ret           (build-ret builder one-plus-one)]
  ;    (dump-module module)
  ;    (is (= "; ModuleID = 'my cool module'\ndefine i32 @0() {\nentry:\n  ret i32 2\n}"
  ;          (print-module-to-string module)))))

  )
