(ns clojure.llvm.interpreter-test
  (:refer-clojure :exclude [eval])
  (:require [clojure.test :refer :all]
            [clojure.llvm.interpreter :refer [eval]]))

(def empty-env
  {:context :expr
   :locals {}
   :namespaces (atom {})})

(deftest something

  (testing "basic addition"
    ;(is (= 0 (eval "(+ 0)")))
    ;(is (= 1 (eval "(+ 1)")))
    ;(is (= 1 (eval "(+ 1 0)")))
    (is (= 2 (eval "(+ 1 1)" empty-env)))
    ;(is (= 3 (eval "(+ 1 2)")))
    ;(is (= 3 (eval "(+ 1 1 1)")))
    )

  )
