
(ns app.components.frontend.ghost-view.prototypes
    (:require [candy.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn view-props-prototype
  ; @param (map) view-props
  ;  {}
  ;
  ; @return (map)
  ;  {}
  [view-props]
  (merge {}
         (param view-props)))
