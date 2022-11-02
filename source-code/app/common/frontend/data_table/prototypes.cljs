
(ns app.common.frontend.data-table.prototypes
    (:require [mid-fruits.candy :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn table-props-prototype
  ; @param (map) table-props
  ;
  ; @return (map)
  ;  {:font-size (keyword)}
  [table-props]
  (merge {:font-size :s}
         (param table-props)))
