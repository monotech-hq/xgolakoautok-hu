
(ns app.products.frontend.picker.prototypes
    (:require [candy.api    :refer [param]]
              [re-frame.api :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn preview-props-prototype
  ; @param (keyword) picker-id
  ; @param (map) picker-props
  ;  {:disabled? (boolean)(opt)
  ;   :multi-select? (boolean)(opt)
  ;   :placeholder (metamorphic-content)(opt)
  ;   :sortable? (boolean)(opt)
  ;   :value-path (vector)}
  ;
  ; @return (map)
  ;  {:disabled? (boolean)
  ;   :items (namespaced maps in vector)
  ;   :indent (map)
  ;   :placeholder (metamorphic-content)
  ;   :sortable? (boolean)
  ;   :value-path (vector)}
  [_ {:keys [disabled? multi-select? placeholder sortable? value-path]}]
  ; XXX#6071
  ; A {:multi-select? false} beállítással használt product-selector value-path
  ; Re-Frame adatbázis útvonalra írt kimenete, egy darab product-link térkép,
  ; amit szükséges vektorba helyezni a product-preview/element komponensnek
  ; items paraméterként történő átadása előtt!
  (let [picked-products @(r/subscribe [:x.db/get-item value-path])]
       {:disabled?   disabled?
        :indent      {:top :m}
        :items       (cond multi-select? picked-products picked-products [picked-products])
        :placeholder placeholder
        :sortable?   sortable?
        :value-path  value-path}))

(defn picker-props-prototype
  ; @param (keyword) picker-id
  ; @param (map) picker-props
  ;
  ; @return (map)
  ;  {}
  [_ picker-props]
  (merge {}
         (param picker-props)))
