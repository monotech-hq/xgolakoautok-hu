
(ns app.schemes.backend.field-editor.prototypes
    (:require [candy.api  :refer [param]]
              [mid-fruits.random :as random]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn field-item-prototype
  ; @param (namespaced map) field-item
  ;
  ; @return (namespaced map)
  ;  {:field/field-id (keyword)
  ;   :field/field-no (integer)}
  [field-item]
  (merge {:field/field-id (random/generate-keyword)
          :field/field-no (random/generate-number 6)}
         (param field-item)))
