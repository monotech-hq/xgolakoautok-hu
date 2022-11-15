
(ns app.vehicle-categories.frontend.viewer.queries)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-save-category-models-query
  ; @param (maps in vector) category-models
  ;
  ; @return (vector)
  [db [_ category-models]]
  (let [category-id (get-in db [:vehicle-categories :viewer/viewed-item :id])]
       [`(~(symbol :vehicle-categories.viewer/save-category-models!)
          ~{:category-id     category-id
            :category-models category-models})]))
