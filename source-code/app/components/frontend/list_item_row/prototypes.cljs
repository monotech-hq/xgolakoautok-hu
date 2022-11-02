
(ns app.components.frontend.list-item-row.prototypes
    (:require [mid-fruits.candy :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn row-props-prototype
  ; @param (map) row-props
  ;  {}
  ;
  ; @return (map)
  ;  {}
  [row-props]
  (merge {}
         (param row-props)))
