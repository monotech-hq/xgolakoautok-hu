
(ns compiler
    (:require [hf.depstar                  :as hf]
              [io.api                      :as io]
              [shadow.cljs.devtools.server :as server]
              [shadow.cljs.devtools.api    :as shadow]
              [x.server-core.api           :as x.core])
    (:gen-class))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (string)
(def JS-CORE-PATH "resources/public/js/core")

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn compile-app!
  ; @param (map) compiler-props
  ; {:java-config (map)
  ;   {:aot (boolean)
  ;    :jar (string)
  ;    :jar-type (keyword)
  ;    :main-class (class)}
  ;  :js-builds (keywords in vector)}
  ;
  ; @usage
  ;  (compiler/compile-app! {:java-config {...}
  ;                          :js-builds [:app-ready]})
  [{:keys [java-config js-builds]}]
  (io/empty-directory! JS-CORE-PATH)
  (doseq [js-build js-builds]
         (println "Compiling:" js-build)
         (shadow/release js-build))
  (hf/jar java-config)
  (x.core/->app-built))
