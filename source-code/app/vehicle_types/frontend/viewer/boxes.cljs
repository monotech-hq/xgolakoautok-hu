
(ns app.vehicle-types.frontend.viewer.boxes
    (:require [app.common.frontend.api     :as common]
              [app.components.frontend.api :as components]
              [app.schemes.frontend.api    :as schemes]
              [app.storage.frontend.api    :as storage]
              [elements.api                :as elements]
              [forms.api                   :as forms]
              [re-frame.api                :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- type-scheme-data
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :vehicle-types.viewer])]
       [schemes/scheme-data :vehicle-types.technical-data {:disabled? viewer-disabled?
                                                           :value-path [:vehicle-types :viewer/viewed-item]}]))

(defn- type-technical-data-box
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :vehicle-types.viewer])]
       [components/surface-box ::type-technical-data-box
                               {:content [:<> [type-scheme-data]
                                              [elements/horizontal-separator {:height :s}]]
                                :disabled? viewer-disabled?
                                :label     :technical-data}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- type-image-picker
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :vehicle-models.viewer])]
       [storage/media-picker ::type-image-picker
                             {:autosave?     false
                              :disabled?     viewer-disabled?
                              :indent        {:top :m :vertical :s}
                              :multi-select? true
                              :placeholder   "n/a"
                              :value-path    [:vehicle-typess :viewer/viewed-item :images]

                              ; TEMP#0051 (source-code/app/common/item_picker/views.cljs)
                              :read-only? true}]))

(defn- type-images-box
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :vehicle-types.viewer])]
       [components/surface-box ::type-images-box
                               {:content [:<> [type-image-picker]
                                              [elements/horizontal-separator {:height :s}]]
                                :disabled? viewer-disabled?
                                :indent    {:top :m}
                                :label     :images}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- type-file-picker
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :vehicle-models.viewer])]
       [storage/media-picker ::type-file-picker
                             {:autosave?     false
                              :disabled?     viewer-disabled?
                              :indent        {:top :m :vertical :s}
                              :multi-select? true
                              :placeholder   "n/a"
                              :value-path    [:vehicle-typess :viewer/viewed-item :files]

                              ; TEMP#0051 (source-code/app/common/item_picker/views.cljs)
                              :read-only? true}]))

(defn- type-files-box
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :vehicle-types.viewer])]
       [components/surface-box ::type-files-box
                               {:content [:<> [type-file-picker]
                                              [elements/horizontal-separator {:height :s}]]
                                :disabled? viewer-disabled?
                                :indent    {:top :m}
                                :label     :files}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- type-transport-cost
  []
  (let [viewer-disabled?    @(r/subscribe [:item-viewer/viewer-disabled? :vehicle-types.viewer])
        type-transport-cost @(r/subscribe [:x.db/get-item [:vehicle-types :viewer/viewed-item :transport-cost]])]
       [components/data-element ::type-transport-cost
                                {:indent      {:top :m :vertical :s}
                                 :label       :transport-cost
                                 :placeholder "n/a"
                                 :value       {:content type-transport-cost :suffix " EUR"}}]))

(defn- type-dealer-rebate
  []
  (let [viewer-disabled?   @(r/subscribe [:item-viewer/viewer-disabled? :vehicle-types.viewer])
        type-dealer-rebate @(r/subscribe [:x.db/get-item [:vehicle-types :viewer/viewed-item :dealer-rebate]])]
       [components/data-element ::type-dealer-rebate
                                {:indent      {:top :m :vertical :s}
                                 :label       :dealer-rebate
                                 :placeholder "n/a"
                                 :value       {:content type-dealer-rebate :suffix " %"}}]))

(defn- type-price-margin
  []
  (let [viewer-disabled?  @(r/subscribe [:item-viewer/viewer-disabled? :vehicle-types.viewer])
        type-price-margin @(r/subscribe [:x.db/get-item [:vehicle-types :viewer/viewed-item :price-margin]])]
       [components/data-element ::type-price-margin
                                {:indent      {:top :m :vertical :s}
                                 :label       :price-margin
                                 :placeholder "n/a"
                                 :value       {:content type-price-margin :suffix " %"}}]))

(defn- type-manufacturer-price
  []
  (let [viewer-disabled?        @(r/subscribe [:item-viewer/viewer-disabled? :vehicle-types.viewer])
        type-manufacturer-price @(r/subscribe [:x.db/get-item [:vehicle-types :viewer/viewed-item :manufacturer-price]])]
       [components/data-element ::type-manufacturer-price
                                {:indent      {:top :m :vertical :s}
                                 :label       :manufacturer-price
                                 :placeholder "n/a"
                                 :value       {:content type-manufacturer-price :suffix " EUR"}}]))

(defn- type-price-box
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :vehicle-types.viewer])]
       [components/surface-box ::type-price-box
                               {:content [:<> [:div (forms/form-row-attributes)
                                                    [:div (forms/form-block-attributes {:ratio 33})
                                                          [type-manufacturer-price]]
                                                    [:div (forms/form-block-attributes {:ratio 33})
                                                          [type-price-margin]]
                                                    [:div (forms/form-block-attributes {:ratio 34})
                                                          [type-dealer-rebate]]
                                               [:div (forms/form-row-attributes)
                                                     [:div (forms/form-block-attributes {:ratio 33})
                                                           [type-transport-cost]]]]
                                              [elements/horizontal-separator {:height :s}]]
                                :disabled? viewer-disabled?
                                :indent    {:top :m}
                                :label     :price}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- type-name
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :vehicle-types.viewer])
        type-name        @(r/subscribe [:x.db/get-item [:vehicle-types :viewer/viewed-item :name]])]
       [components/data-element ::type-name
                                {:indent      {:top :m :vertical :s}
                                 :label       :name
                                 :placeholder "n/a"
                                 :value       type-name}]))

(defn- type-basic-data-box
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :vehicle-types.viewer])]
       [components/surface-box ::type-basic-data-box
                               {:content [:<> [:div (forms/form-row-attributes)
                                                    [:div (forms/form-block-attributes {:ratio 100})
                                                          [type-name]]]
                                              [elements/horizontal-separator {:height :s}]]
                                :disabled? viewer-disabled?
                                :label     :basic-data}]))
