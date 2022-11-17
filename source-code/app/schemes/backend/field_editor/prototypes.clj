
(ns app.schemes.backend.field-editor.prototypes
    (:require [candy.api  :refer [param]]
              [random.api :as random]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn field-item-prototype
  ; @param (namespaced map) field-item
  ;
  ; @return (namespaced map)
  ;  {:field/field-id (keyword)
  ;   :field/field-no (integer)
  ;   :field/type (keyword)}
  [field-item]
  (merge {:field/field-id (random/generate-keyword)
          :field/field-no (random/generate-number 6)
          :field/type     :single-field}
         (param field-item)))
