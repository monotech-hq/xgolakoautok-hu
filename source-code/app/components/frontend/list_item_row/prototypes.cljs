
(ns app.components.frontend.list-item-row.prototypes
    (:require [candy.api :refer [param]]))

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
