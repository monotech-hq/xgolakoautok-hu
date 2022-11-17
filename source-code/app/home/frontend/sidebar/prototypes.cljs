
(ns app.home.frontend.sidebar.prototypes
    (:require [app.home.frontend.handler.config :as handler.config]
              [candy.api                        :refer [param]]
              [vector.api                       :as vector]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn item-props-prototype
  ; @param (map) item-props
  ;  {:group-name (metamorphic-content)}
  ;
  ; @return (map)
  ;  {:group-name (metamorphic-content)}
  [{:keys [group-name] :as item-props}]
  (merge {:icon-family :material-icons-filled}
         (param item-props)
         (if-not (letfn [(f [{:keys [name]}] (= group-name name))]
                        (vector/any-item-match? handler.config/GROUP-ORDER f))
                 {:group-name :other})))
