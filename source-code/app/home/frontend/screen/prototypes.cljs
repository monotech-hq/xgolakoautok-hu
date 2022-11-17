
(ns app.home.frontend.screen.prototypes
    (:require [app.home.frontend.handler.config :as handler.config]
              [candy.api                        :refer [param]]
              [vector.api                       :as vector]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn item-props-prototype
  ; @param (map) item-props
  ;  {:group-name (metamorphic-content)(opt)}
  ;
  ; @return (map)
  ;  {:color (keyword)
  ;   :group-name (metamorphic-content)
  ;   :horizontal-weight (integer)
  ;   :icon-family (keyword)}
  [{:keys [group-name] :as item-props}]
  (merge {:color             :default
          :horizontal-weight 0
          :icon-family       :material-icons-filled}
         (param item-props)
         (if-not (letfn [(f [{:keys [name]}] (= group-name name))]
                        (vector/any-item-match? handler.config/GROUP-ORDER f))
                 {:group-name :other})))
