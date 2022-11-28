
(ns app.common.frontend.item-viewer.prototypes
    (:require [candy.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn body-props-prototype
  ; @param (keyword) viewer-id
  ; @param (map) body-props
  ;  {}
  ;
  ; @return (map)
  ;  {:auto-title? (boolean)
  ;   :label-key (keyword)}
  [_ {:keys [] :as body-props}]
  (merge {:auto-title? true
          :label-key   :name}
         (param body-props)))
