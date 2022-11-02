
(ns app.categories.frontend.viewer.queries)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-save-category-models-query
  ; @param (maps in vector) category-models
  ;
  ; @return (vector)
  [db [_ category-models]]
  (let [category-id (get-in db [:categories :viewer/viewed-item :id])]
       [`(~(symbol :categories.viewer/save-category-models!)
          ~{:category-id     category-id
            :category-models category-models})]))
