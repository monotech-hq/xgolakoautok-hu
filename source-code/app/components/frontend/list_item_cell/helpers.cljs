
(ns app.components.frontend.list-item-cell.helpers)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn cell-attributes
  ; @param (keyword) cell-id
  ; @param (map) cell-props
  ;  {:width (keyword or string)(opt)}
  ;
  ; @return (map)
  ;  {:style (map)}
  [_ {:keys [width]}]
  ;:display :table-cell
  (case width :stretch {:style {:flex-grow 1}}
                       {:style {:width width}}))
