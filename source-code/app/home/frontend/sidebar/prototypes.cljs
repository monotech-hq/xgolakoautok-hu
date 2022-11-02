
(ns app.home.frontend.sidebar.prototypes
    (:require [app.home.frontend.handler.config :as handler.config]
              [mid-fruits.candy                 :refer [param]]
              [mid-fruits.vector                :as vector]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn item-props-prototype
  ; @param (map) item-props
  ;  {}
  ;
  ; @return (map)
  ;  {}
  [{:keys [group] :as item-props}]
  (merge {:icon-family :material-icons-filled}
         (param item-props)
         (if-not (vector/contains-item? handler.config/GROUP-ORDER group)
                 {:group :other})))
