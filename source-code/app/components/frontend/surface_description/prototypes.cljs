
(ns app.components.frontend.surface-description.prototypes
    (:require [candy.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn description-props-prototype
  ; @param (map) description-props
  ;
  ; @return (map)
  ;  {:horizontal-align (keyword)
  ;   :indent (map)}
  [description-props]
  (merge {:horizontal-align :center
          :indent           {:horizontal :xxs}}
         (param description-props)))
