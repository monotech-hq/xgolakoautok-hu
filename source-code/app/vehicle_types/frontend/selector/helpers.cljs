
(ns app.vehicle-types.frontend.selector.helpers)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn import-id-f
  ; @param (namespaced map) type-link
  ;  {:type/id (string)}
  ;
  ; @example
  ;  (import-id-f {:type/id "my-type"})
  ;  =>
  ;  "my-type"
  ;
  ; @return (string)
  [type-link]
  (:type/id type-link))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn export-item-f
  ; @param (string) type-id
  ; @param (map) type-item
  ; @param (integer) type-count
  ;
  ; @example
  ;  (export-item-f "my-type" {...} 1)
  ;  =>
  ;  {:type/id "my-type"}
  ;
  ; @return (map)
  ;  {:type/id (string)}
  [type-id _ _]
  (if type-id {:type/id type-id}))
