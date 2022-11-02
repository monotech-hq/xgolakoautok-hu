
(ns app.common.frontend.action-bar.prototypes
    (:require [mid-fruits.candy :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn bar-props-prototype
  ; @param (map) bar-props
  ;
  ; @return (map)
  [bar-props]
  (merge {}
         (param bar-props)))
