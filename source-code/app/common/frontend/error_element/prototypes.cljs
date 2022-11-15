
(ns app.common.frontend.error-element.prototypes
    (:require [candy.api :refer [param]]))

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
