
(ns app.schemes.backend.form-handler.prototypes
    (:require [app.schemes.backend.field-editor.prototypes :as field-editor.prototypes]
              [candy.api                                   :refer [param]]
              [vector.api                           :as vector]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn scheme-item-prototype
  ; @param (keyword) scheme-id
  ; @param (namespaced map) scheme-item
  ;  {:scheme/fields (namespaced maps in vector)}
  ;
  ; @usage
  ;  (scheme-item-prototype :my-scheme {...})
  ;
  ; @return (namespaced map)
  ;  {:scheme/fields (namespaced maps in vector)}
  [scheme-id {:scheme/keys [fields] :as scheme-item}]
  (merge {:scheme/scheme-id scheme-id}
         (param scheme-item)
         {:scheme/fields (vector/->items fields field-editor.prototypes/field-item-prototype)}))
