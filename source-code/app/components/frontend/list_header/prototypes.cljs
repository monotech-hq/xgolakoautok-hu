
(ns app.components.frontend.list-header.prototypes
    (:require [mid-fruits.candy :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn header-props-prototype
  ; @param (map) header-props
  ;  {}
  ;
  ; @return (map)
  ;  {}
  [header-props]
  (merge {}
         (param header-props)))
