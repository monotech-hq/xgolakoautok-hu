
(ns app.schemes.frontend.field-deleter.queries)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-delete-field-query
  ; @param (keyword) scheme-id
  ; @param (keyword) field-id
  ;
  ; @return (vector)
  [_ [_ scheme-id field-id]]
  [`(~(symbol "schemes.field-deleter/delete-field!")
     ~{:field-id  field-id
       :scheme-id scheme-id})])
