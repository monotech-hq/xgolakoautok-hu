
(ns app.common.frontend.ghost-element.prototypes
    (:require [mid-fruits.candy :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn element-props-prototype
  ; @param (map) element-props
  ;  {}
  ;
  ; @return (map)
  ;  {}
  [element-props]
  (merge {}
         (param element-props)))
