(ns clojure.llvm.emitter
  (:require [llvm.core :refer :all]))

(defmulti -emit (fn [{:keys [op]} _] op))

(defn codegen-truth [{:keys [context]} value]
  (const-i-cmp 33 value (const-int (get-int-32-ty-in-context context) 0 false)))

(defmethod -emit :do [{:keys [statements ret]} {:keys [context module builder init-function] :as frame}]
  (let [before-block  (get-insert-block builder)
        int32-type    (get-int-32-ty-in-context context)
        function-type (function-type int32-type [] 0 false)
        func          (add-function module "do" function-type)
        basic-block   (append-basic-block-in-context context func "entry")
        func-frame    (assoc frame :current-function func)
        _             (position-builder-at-end builder basic-block)
        _             (doseq [statement statements] (-emit statement func-frame))
        ret-value     (-emit ret func-frame)
        _             (build-ret builder ret-value)]
    (position-builder-at-end builder before-block)
    (build-call builder func [] 0 "calltmp")))

(defmethod -emit :if [ast {:keys [context module builder current-function] :as frame}]
  (let [cond-value  (-emit (:test ast) frame)
        truthy-test (codegen-truth frame cond-value)
        then-bb     (append-basic-block-in-context context current-function "then")
        else-bb     (append-basic-block-in-context context current-function "else")
        merge-bb    (append-basic-block-in-context context current-function "ifcont")
        _           (build-cond-br builder truthy-test then-bb else-bb)
        _           (position-builder-at-end builder then-bb)
        then-value  (-emit (:then ast) frame)
        _           (build-br builder merge-bb)
        then-insert (get-insert-block builder)
        _           (position-builder-at-end builder else-bb)
        else-value  (-emit (:else ast) frame)
        _           (build-br builder merge-bb)
        else-insert (get-insert-block builder)
        _           (position-builder-at-end builder merge-bb)
        int32-type  (get-int-32-ty-in-context context)
        phi-node    (build-phi builder int32-type "iftmp")
        _           (add-incoming phi-node [then-value] [then-insert] 1)
        _           (add-incoming phi-node [else-value] [else-insert] 1)]
    phi-node))

(defmulti -invoke (fn [{:keys [fn]} _] (:form fn)))

(defmethod -invoke '= [ast {:keys [context] :as frame}]
  (let [lhs (-emit (first (:args ast)) frame)
        rhs (-emit (second (:args ast)) frame)]
    (const-i-cmp 32 lhs rhs)))

(defmethod -invoke '+ [ast {:keys [context] :as frame}]
  (let [lhs (-emit (first (:args ast)) frame)
        rhs (-emit (second (:args ast)) frame)]
    (const-add lhs rhs)))

(defmethod -emit :invoke [ast frame]
  (-invoke ast frame))

(defmulti -emit-const (fn [{:keys [type]} _] type))

(defmethod -emit-const :number [ast {:keys [context]}]
  (const-int (get-int-32-ty-in-context context) (:form ast) false))

(defmethod -emit :const [ast frame]
  (-emit-const ast frame))

(defn emit [ast frame]
  (do (-emit ast frame))
  frame)
