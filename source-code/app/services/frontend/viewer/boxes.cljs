
(ns app.services.frontend.viewer.boxes
    (:require [app.common.frontend.api     :as common]
              [app.components.frontend.api :as components]
              [app.storage.frontend.api    :as storage]
              [elements.api                :as elements]
              [forms.api                   :as forms]
              [re-frame.api                :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- service-thumbnail-picker
  []
  (let [viewer-disabled?  @(r/subscribe [:item-viewer/viewer-disabled? :services.viewer])]
       [storage/media-picker ::service-thumbnail-picker
                             {:autosave?     true
                              :disabled?     viewer-disabled?
                              :indent        {:top :m :vertical :s}
                              :multi-select? false
                              :placeholder   "n/a"
                              :value-path    [:services :viewer/viewed-item :thumbnail]

                              ; TEMP#0051 (source-code/app/common/item_picker/views.cljs)
                              :read-only? true}]))

(defn- service-thumbnail-box
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :services.viewer])]
       [components/surface-box ::service-thumbnail-box
                               {:content [:<> [service-thumbnail-picker]
                                              [elements/horizontal-separator {:height :s}]]
                                :disabled? viewer-disabled?
                                :indent    {:top :m}
                                :label     :thumbnail}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- service-quantity-unit
  []
  (let [viewer-disabled?      @(r/subscribe [:item-viewer/viewer-disabled? :services.viewer])
        service-quantity-unit @(r/subscribe [:x.db/get-item [:services :viewer/viewed-item :quantity-unit :label]])]
       [components/data-element ::service-quantity-unit
                                {:disabled?   viewer-disabled?
                                 :indent      {:top :m :vertical :s}
                                 :label       :quantity-unit
                                 :placeholder "n/a"
                                 :value       service-quantity-unit}]))

(defn- service-description
  []
  (let [viewer-disabled?    @(r/subscribe [:item-viewer/viewer-disabled? :services.viewer])
        service-description @(r/subscribe [:x.db/get-item [:services :viewer/viewed-item :description]])]
       [components/data-element ::service-description
                                {:disabled?   viewer-disabled?
                                 :indent      {:top :m :vertical :s}
                                 :label       :description
                                 :placeholder "n/a"
                                 :value       service-description}]))

(defn- service-item-number
  []
  (let [viewer-disabled?    @(r/subscribe [:item-viewer/viewer-disabled? :services.viewer])
        service-item-number @(r/subscribe [:x.db/get-item [:services :viewer/viewed-item :item-number]])]
       [components/data-element ::service-item-number
                                {:copyable?   true
                                 :disabled?   viewer-disabled?
                                 :indent      {:top :m :vertical :s}
                                 :label       :item-number
                                 :marked?     true
                                 :placeholder "n/a"
                                 :value       service-item-number}]))

(defn- service-price
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :services.viewer])
        service-price    @(r/subscribe [:x.db/get-item [:services :viewer/viewed-item :unit-price]])]
       [components/data-element ::service-price
                                {:disabled?   viewer-disabled?
                                 :indent      {:top :m :vertical :s}
                                 :label       :unit-price
                                 :placeholder "n/a"
                                 :value       {:content service-price :suffix " EUR"}}]))

(defn- service-name
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :services.viewer])
        service-name     @(r/subscribe [:x.db/get-item [:services :viewer/viewed-item :name]])]
       [components/data-element ::service-name
                                {:disabled?   viewer-disabled?
                                 :indent      {:top :m :vertical :s}
                                 :label       :name
                                 :placeholder "n/a"
                                 :value       service-name}]))

(defn- service-data-box
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :services.viewer])]
       [components/surface-box ::service-data-box
                               {:content [:<> [:div (forms/form-row-attributes)
                                                    [:div (forms/form-block-attributes {:ratio 100})
                                                          [service-name]]]
                                              [:div (forms/form-row-attributes)
                                                    [:div (forms/form-block-attributes {:ratio 33})
                                                          [service-quantity-unit]]
                                                    [:div (forms/form-block-attributes {:ratio 33})
                                                          [service-price]]
                                                    [:div (forms/form-block-attributes {:ratio 33})
                                                          [service-item-number]]]
                                              [:div (forms/form-row-attributes)
                                                    [:div (forms/form-block-attributes {:ratio 100})
                                                          [service-description]]]
                                              [elements/horizontal-separator {:height :s}]]
                                :disabled? viewer-disabled?
                                :label     :data}]))
