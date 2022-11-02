
(ns app.price-quote-templates.frontend.selector.helpers)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn import-id-f
  ; @param (namespaced map) template-link
  ;  {:template/id (string)}
  ;
  ; @example
  ;  (selector.helpers/import-id-f {:template/id "my-template"})
  ;  =>
  ;  "my-template"
  ;
  ; @return (string)
  [template-link]
  (:template/id template-link))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn export-item-f
  ; @param (string) template-id
  ; @param (map) template-item
  ; @param (integer) template-count
  ;
  ; @example
  ;  (selector.helpers/export-item-f "my-template" {...} 1)
  ;  =>
  ;  {:template/id "my-template"}
  ;
  ; @return (map)
  ;  {:template/id (string)}
  [template-id _ _]
  (if template-id {:template/id template-id}))
