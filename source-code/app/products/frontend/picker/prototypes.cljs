
(ns app.products.frontend.picker.prototypes
    (:require [candy.api :refer [param]]
              [re-frame.api     :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn preview-props-prototype
  ; @param (keyword) picker-id
  ; @param (map) picker-props
  ;  {}
  ;
  ; @return (map)
  ;  {}
  [_ {:keys [disabled? multi-select? placeholder value-path]}]
  ; XXX#6071
  ; A {:multi-select? false} beállítással használt product-selector value-path
  ; Re-Frame adatbázis útvonalra írt kimenete, egy darab product-link térkép,
  ; amit szükséges vektorba helyezni a product-preview/element komponensnek
  ; items paraméterként történő átadása előtt!
  (let [picked-products @(r/subscribe [:db/get-item value-path])]
       {:disabled?   disabled?
        :indent      {:top :m}
        :items       (cond multi-select? picked-products picked-products [picked-products])
        :placeholder placeholder}))

(defn picker-props-prototype
  ; @param (keyword) picker-id
  ; @param (map) picker-props
  ;
  ; @return (map)
  ;  {}
  [_ picker-props]
  (merge {}
         (param picker-props)))
