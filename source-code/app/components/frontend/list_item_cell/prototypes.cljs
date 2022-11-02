
(ns app.components.frontend.list-item-cell.prototypes
    (:require [mid-fruits.candy :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn cell-props-prototype
  ; @param (map) cell-props
  ;  {}
  ;
  ; @return (map)
  ;  {}
  [cell-props]
  (merge {}
         (param cell-props)))
