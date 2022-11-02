
(ns app.home.frontend.screen.prototypes
    (:require [app.home.frontend.handler.config :as handler.config]
              [mid-fruits.candy                 :refer [param]]
              [mid-fruits.vector                :as vector]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn item-props-prototype
  ; @param (map) item-props
  ;  {:group (keyword)(opt)}
  ;
  ; @return (map)
  ;  {:color (keyword)
  ;   :group (keyword)
  ;   :horizontal-weight (integer)
  ;   :icon-family (keyword)}
  [{:keys [group] :as item-props}]
  (merge {:color             :default
          :horizontal-weight 0
          :icon-family       :material-icons-filled}
         (param item-props)
         (if-not (vector/contains-item? handler.config/GROUP-ORDER group)
                 {:group :other})))
