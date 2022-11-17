
(ns site.components.frontend.scheme-table.queries)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn request-scheme-form-query
  ; @param (keyword) scheme-id
  ;
  ; @example
  ;  (request-scheme-form-query :my-scheme)
  ;  =>
  ;  [`~(:components.scheme-table/get-scheme-form {:scheme-id :my-scheme})]
  ;
  ; @return (vector)
  [scheme-id]
  (let [resolver-id    :components.scheme-table/get-scheme-form
        resolver-props {:scheme-id scheme-id}]
       [`(~resolver-id ~resolver-props)]))
