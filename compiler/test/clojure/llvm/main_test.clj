(ns clojure.llvm.main-test
  (:require [clojure.test      :refer :all]
            [clojure.llvm.main :refer :all]))

(deftest parse-args-test
  (testing "parses one search path"
    (is (= ["one"]
           (:search-paths (parse-args ["-I" "one"])))))

  (testing "parses two search paths"
    (is (= ["one" "two"]
           (:search-paths (parse-args ["-I" "one" "-I" "two"])))))

  (testing "ignores -I when no value is given"
    (is (= ["one"]
           (:search-paths (parse-args ["-I" "one" "-I"])))))

  (testing "parses one input filename"
    (is (= ["one"]
           (:input-filenames (parse-args ["one"])))))

  (testing "parses two input filenames"
    (is (= ["one"]
           (:input-filenames (parse-args ["one"])))))

  (testing "parses output-file"
    (is (= "some-file"
           (:output-file (parse-args ["-o" "some-file"])))))

  (testing "ignores -o when no value is given"
    (is (= nil
           (:output-file (parse-args ["-o"])))))

  (testing "parses main"
    (is (= "some-main"
           (:main (parse-args ["-m" "some-main"])))))

  (testing "ignores -m when no value is given"
    (is (= nil
           (:main (parse-args ["-m"])))))

  (testing "all together"
    (is (= {:search-paths    ["one" "two"]
            :input-filenames ["three" "four"]
            :output-file     "out"
            :main            "some.main"}
           (parse-args ["-I" "one" "-I" "two" "-o" "out" "-m" "some.main" "three" "four"]))))
  )
