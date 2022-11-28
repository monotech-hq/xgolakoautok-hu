
(ns app.components.frontend.surface-box.prototypes
    (:require [candy.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn box-props-prototype
  ; @param (map) box-props
  ;  {}
  ;
  ; @return (map)
  ;  {:background-color (string)}
  [{:keys [] :as box-props}]
  (merge {:background-color "var( --fill-color )"}
         (param box-props)))
