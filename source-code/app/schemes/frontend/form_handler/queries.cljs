
(ns app.schemes.frontend.form-handler.queries)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn request-scheme-form-query
  ; @param (keyword) scheme-id
  ;
  ; @example
  ;  (request-scheme-form-query :my-scheme)
  ;  =>
  ;  [`~(:schemes.form-handler/get-scheme-form {:scheme-id :my-scheme})]
  ;
  ; @return (vector)
  [scheme-id]
  (let [resolver-id    :schemes.form-handler/get-scheme-form
        resolver-props {:scheme-id scheme-id}]
       [`(~resolver-id ~resolver-props)]))
