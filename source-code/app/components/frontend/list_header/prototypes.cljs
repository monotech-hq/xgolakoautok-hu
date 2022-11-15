
(ns app.components.frontend.list-header.prototypes
    (:require [candy.api :refer [param]]))

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
