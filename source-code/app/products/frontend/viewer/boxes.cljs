
(ns app.products.frontend.viewer.boxes
    (:require [app.common.frontend.api     :as common]
              [app.components.frontend.api :as components]
              [app.storage.frontend.api    :as storage]
              [elements.api                :as elements]
              [forms.api                   :as forms]
              [re-frame.api                :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- product-thumbnail-picker
  []
  (let [viewer-disabled?  @(r/subscribe [:item-viewer/viewer-disabled? :products.viewer])]
       [storage/media-picker ::product-thumbnail-picker
                             {:autosave?     true
                              :disabled?     viewer-disabled?
                              :indent        {:top :m :vertical :s}
                              :multi-select? false
                              :placeholder   "n/a"
                              :value-path    [:products :viewer/viewed-item :thumbnail]

                              ; TEMP#0051 (source-code/app/common/item_picker/views.cljs)
                              :read-only? true}]))

(defn- product-thumbnail-box
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :products.viewer])]
       [components/surface-box ::product-thumbnail-box
                               {:content [:<> [product-thumbnail-picker]
                                              [elements/horizontal-separator {:height :s}]]
                                :disabled? viewer-disabled?
                                :indent    {:top :m}
                                :label     :thumbnail}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- product-images-picker
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :products.viewer])]
       [storage/media-picker ::product-images-picker
                             {:autosave?     false
                              :disabled?     viewer-disabled?
                              :indent        {:top :m :vertical :s}
                              :multi-select? true
                              :placeholder   "n/a"
                              :value-path    [:products :viewer/viewed-item :images]

                              ; TEMP#0051 (source-code/app/common/item_picker/views.cljs)
                              :read-only? true}]))

(defn- product-images-box
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :products.viewer])]
       [components/surface-box ::product-images-box
                               {:content [:<> [product-images-picker]
                                              [elements/horizontal-separator {:height :s}]]
                                :disabled? viewer-disabled?
                                :indent    {:top :m}
                                :label     :images}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- product-quantity-unit
  []
  (let [viewer-disabled?      @(r/subscribe [:item-viewer/viewer-disabled? :products.viewer])
        product-quantity-unit @(r/subscribe [:x.db/get-item [:products :viewer/viewed-item :quantity-unit :label]])]
       [components/data-element ::product-quantity-unit
                                {:disabled?   viewer-disabled?
                                 :indent      {:top :m :vertical :s}
                                 :label       :quantity-unit
                                 :placeholder "n/a"
                                 :value       product-quantity-unit}]))

(defn- product-description
  []
  (let [viewer-disabled?    @(r/subscribe [:item-viewer/viewer-disabled? :products.viewer])
        product-description @(r/subscribe [:x.db/get-item [:products :viewer/viewed-item :description]])]
       [components/data-element ::product-description
                                {:disabled?   viewer-disabled?
                                 :indent      {:top :m :vertical :s}
                                 :label       :description
                                 :placeholder "n/a"
                                 :value       product-description}]))

(defn- product-item-number
  []
  (let [viewer-disabled?    @(r/subscribe [:item-viewer/viewer-disabled? :products.viewer])
        product-item-number @(r/subscribe [:x.db/get-item [:products :viewer/viewed-item :item-number]])]
       [components/data-element ::product-item-number
                                {:copyable?   true
                                 :disabled?   viewer-disabled?
                                 :indent      {:top :m :vertical :s}
                                 :label       :item-number
                                 :marked?     true
                                 :placeholder "n/a"
                                 :value       product-item-number}]))

(defn- product-price
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :products.viewer])
        product-price    @(r/subscribe [:x.db/get-item [:products :viewer/viewed-item :unit-price]])]
       [components/data-element ::product-price
                                {:disabled?   viewer-disabled?
                                 :indent      {:top :m :vertical :s}
                                 :label       :unit-price
                                 :placeholder "n/a"
                                 :value       {:content product-price :suffix " EUR"}}]))

(defn- product-name
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :products.viewer])
        product-name     @(r/subscribe [:x.db/get-item [:products :viewer/viewed-item :name]])]
       [components/data-element ::product-name
                                {:disabled?   viewer-disabled?
                                 :indent      {:top :m :vertical :s}
                                 :label       :name
                                 :placeholder "n/a"
                                 :value       product-name}]))

(defn- product-data-box
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :products.viewer])]
       [components/surface-box ::product-data-box
                               {:content [:<> [:div (forms/form-row-attributes)
                                                    [:div (forms/form-block-attributes {:ratio 100})
                                                          [product-name]]]
                                              [:div (forms/form-row-attributes)
                                                    [:div (forms/form-block-attributes {:ratio 33})
                                                          [product-quantity-unit]]
                                                    [:div (forms/form-block-attributes {:ratio 33})
                                                          [product-price]]
                                                    [:div (forms/form-block-attributes {:ratio 34})
                                                          [product-item-number]]]
                                              [:div (forms/form-row-attributes)
                                                    [:div (forms/form-block-attributes {:ratio 100})
                                                          [product-description]]]
                                              [elements/horizontal-separator {:height :s}]]
                                :disabled? viewer-disabled?
                                :label     :data}]))
