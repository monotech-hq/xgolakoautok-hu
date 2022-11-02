
(ns app.schemes.frontend.field-editor.queries)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-save-field-query
  ; @param (keyword) scheme-id
  ; @param (keyword or nil) field-id
  ;
  ; @return (vector)
  [db [_ scheme-id _]]
  (let [field-item (get-in db [:schemes :field-editor/field-item])]
       [`(~(symbol "schemes.field-editor/save-field!")
          ~{:field-item field-item
            :scheme-id  scheme-id})]))
