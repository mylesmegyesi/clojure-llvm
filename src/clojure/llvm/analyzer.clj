(ns clojure.llvm.analyzer
  (:refer-clojure :exclude [macroexpand-1])
  (:require [clojure.tools.analyzer                         :as ana]
            [clojure.tools.analyzer.utils                   :refer [ctx maybe-var]]
            [clojure.tools.analyzer.passes                  :refer [walk prewalk postwalk]]
            [clojure.tools.analyzer.passes.source-info      :refer [source-info]]
            [clojure.tools.analyzer.passes.cleanup          :refer [cleanup1 cleanup2]]
            [clojure.tools.analyzer.passes.elide-meta       :refer [elide-meta]]
            [clojure.tools.analyzer.passes.constant-lifter  :refer [constant-lift]]
            [clojure.tools.analyzer.passes.warn-earmuff     :refer [warn-earmuff]]
            [clojure.tools.analyzer.passes.collect          :refer [collect]]
            [clojure.tools.analyzer.passes.add-binding-atom :refer [add-binding-atom]]
            [clojure.tools.analyzer.passes.uniquify         :refer [uniquify-locals]]))

(def specials ana/specials)

(defmulti parse (fn [[op & rest] env] op))

(defmethod parse :default
  [form env]
  (ana/-parse form env))

(defn desugar-host-expr [form env]
  form)

(defn macroexpand-1 [form env]
  (if (seq? form)
    (let [op (first form)]
      (if (specials op)
        form
        (let [v (maybe-var op env)
              m (meta v)
              local? (-> env :locals (get op))
              macro? (and (not local?) (:macro m))
              inline-arities-f (:inline-arities m)
              args (rest form)
              inline? (and (not local?)
                           (or (not inline-arities-f)
                               (inline-arities-f (count args)))
                           (:inline m))]
          (cond

           macro?
           (apply v form env (rest form)) ; (m &form &env & args)

           inline?
           (vary-meta (apply inline? args) merge m)

           :else
           (desugar-host-expr form env)))))
    (desugar-host-expr form env)))

(defn create-var [sym {:keys [ns]}]
  (intern ns sym))

(defn analyze
  "Given an environment, a map containing
   -  :locals (mapping of names to lexical bindings),
   -  :context (one of :statement, :expr or :return
 and form, returns an expression object (a map containing at least :form, :op and :env keys)."
  [form env]
  (binding [ana/macroexpand-1 macroexpand-1
            ana/create-var    create-var
            ana/parse         parse]
    (-> (ana/analyze form env)

      uniquify-locals
      add-binding-atom

      (walk (fn [ast]
              (-> ast
                cleanup1
                warn-earmuff
                source-info
                elide-meta))
            constant-lift)

      (prewalk
       (comp cleanup2
          (collect :constants
                   :callsites
                   :closed-overs))))))
