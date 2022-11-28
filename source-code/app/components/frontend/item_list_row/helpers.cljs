
(ns app.components.frontend.item-list-row.helpers)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn row-attributes
  ; @param (keyword) row-id
  ; @param (map) row-props
  ;  {:disabled? (boolean)(opt)
  ;   :drag-attributes (map)(opt)}
  ;
  ; @return (map)
  ;  {:style (map)}
  [_ {:keys [disabled? drag-attributes]}]
  (cond-> {}
          disabled?       (merge {:style {:opacity ".5"}})
          drag-attributes (merge drag-attributes)))
