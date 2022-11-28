
(ns app.components.frontend.list-item-gap.prototypes
    (:require [candy.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn gap-props-prototype
  ; @param (map) row-props
  ;  {:width (px)}
  ;
  ; @return (map)
  ;  {:width (px)}
  [gap-props]
  (merge {:width 12}
         (param gap-props)))
