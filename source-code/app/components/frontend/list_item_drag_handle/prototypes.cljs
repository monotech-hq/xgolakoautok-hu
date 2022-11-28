
(ns app.components.frontend.list-item-drag-handle.prototypes
    (:require [candy.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn handle-props-prototype
  ; @param (map) handle-props
  ;
  ; @return (map)
  [handle-props]
  (merge {}
         (param handle-props)))
