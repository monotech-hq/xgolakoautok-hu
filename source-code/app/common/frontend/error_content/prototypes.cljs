
(ns app.common.frontend.error-content.prototypes
    (:require [mid-fruits.candy :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn content-props-prototype
  ; @param (map) content-props
  ;  {}
  ;
  ; @return (map)
  ;  {}
  [content-props]
  (merge {}
         (param content-props)))
