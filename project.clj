(defproject clojure.tools.compiler.llvm "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :dependencies [[org.clojure/clojure "1.5.1"]
                 [com.nativelibs4java/bridj "0.6.2"]]

  :profiles {:dev {:dependencies [[com.aphyr/prism "0.1.1"]]
                   :plugins [[com.aphyr/prism "0.1.1"]]}}

  )
