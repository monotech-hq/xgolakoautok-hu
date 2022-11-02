
(ns app.models.frontend.selector.helpers)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn import-id-f
  ; @param (namespaced map) model-link
  ;  {:model/id (string)}
  ;
  ; @example
  ;  (selector.helpers/import-id-f {:model/id "my-model"})
  ;  =>
  ;  "my-model"
  ;
  ; @return (string)
  [model-link]
  (:model/id model-link))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn export-item-f
  ; @param (string) model-id
  ; @param (map) model-item
  ; @param (integer) model-count
  ;
  ; @example
  ;  (selector.helpers/export-item-f "my-model" {...} 1)
  ;  =>
  ;  {:model/id "my-model"}
  ;
  ; @return (map)
  ;  {:model/id (string)}
  [model-id _ _]
  (if model-id {:model/id model-id}))
