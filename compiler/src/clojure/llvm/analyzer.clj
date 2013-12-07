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

(defn macroexpand-1 [form env] form)

(defn create-var [sym {:keys [ns]}]
  (keyword sym))

(defn analyze [form env]
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
