
(ns app.components.frontend.error-content.prototypes
    (:require [candy.api :refer [param]]))

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
