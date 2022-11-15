
(ns app.schemes.frontend.field-sorter.queries)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-reorder-fields-query
  ; @param (keyword) scheme-id
  ; @param (namespaced map in vector) reordered-fields
  ;
  ; @return (vector)
  [scheme-id reordered-fields]
  [`(~(symbol "schemes.field-sorter/reorder-fields!")
     ~{:scheme-id        scheme-id
       :reordered-fields reordered-fields})])
