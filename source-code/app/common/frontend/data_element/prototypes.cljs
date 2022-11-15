
(ns app.common.frontend.data-element.prototypes
    (:require [candy.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn element-props-prototype
  ; @param (map) element-props
  ;
  ; @return (map)
  ;  {:font-size (keyword)}
  [element-props]
  (merge {:font-size :s}
         (param element-props)))
