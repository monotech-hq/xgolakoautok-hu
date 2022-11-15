
(ns app.schemes.frontend.field-sorter.events)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------
 
(defn reorder-fields!
  ; @param (keyword) scheme-id
  ; @param (namespaced maps in vector) reordered-fields
  ;
  ; @return (map)
  [db [_ scheme-id reordered-fields]]
  (assoc-in db [:schemes :form-handler/scheme-forms scheme-id :scheme/fields] reordered-fields))
