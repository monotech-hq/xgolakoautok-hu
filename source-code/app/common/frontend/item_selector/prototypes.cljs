
(ns app.common.frontend.item-selector.prototypes
    (:require [mid-fruits.candy :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn selector-props-prototype
  ; @param (map) selector-props
  ;
  ; @return (map)
  ;  {:export-item-f (function)
  ;   :import-count-f (function)
  ;   :import-id-f (function)}
  [selector-props]
  (merge {:export-item-f  (fn [item-id item item-count] item-id)
          :import-id-f    (fn [item-id]                 item-id)
          :import-count-f (fn [_]                             1)}
         (param selector-props)))
