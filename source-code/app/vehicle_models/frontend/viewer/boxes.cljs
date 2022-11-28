
(ns app.vehicle-models.frontend.viewer.boxes
    (:require [app.common.frontend.api        :as common]
              [app.components.frontend.api    :as components]
              [app.storage.frontend.api       :as storage]
              [app.vehicle-types.frontend.api :as vehicle-types]
              [elements.api                   :as elements]
              [forms.api                      :as forms]
              [re-frame.api                   :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- create-vehicle-type-button
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :vehicle-models.viewer])
        model-id         @(r/subscribe [:x.router/get-current-route-path-param :item-id])
        on-click [:x.router/go-to! (str "/@app-home/vehicle-models/"model-id"/types/create")]]
       [:div {:style {:display "flex"}}
             [elements/button ::create-vehicle-type-button
                              {:color     :muted
                               :disabled? viewer-disabled?
                               :font-size :xs
                               :indent    {:vertical :s :bottom :m}
                               :label     :add-vehicle-type!
                               :on-click  on-click}]]))

(defn- model-vehicle-type-picker
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :vehicle-models.viewer])]
       [vehicle-types/type-picker ::model-vehicle-type-picker
                                  {:autosave?     false
                                   :disabled?     viewer-disabled?
                                   :indent        {:vertical :s}
                                   :multi-select? true
                                   :placeholder   "n/a"
                                   :sortable?     true
                                   :toggle-label  :select-vehicle-types!
                                   :value-path    [:vehicle-models :viewer/viewed-item :types]}]))

(defn- model-vehicle-type-lister
  []
  [vehicle-types/type-lister])

(defn- model-vehicle-types-box
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :vehicle-models.viewer])]
       [components/surface-box ::model-vehicle-types-box
                               {:content   [:<> [create-vehicle-type-button]
                                                [:div {:style {:padding "0 18px"}}
                                                      [model-vehicle-type-lister]]
                                              [elements/horizontal-separator {:height :xxs}]]
                                :disabled? viewer-disabled?
                                :label     :vehicle-types}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- model-thumbnail-picker
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :vehicle-models.viewer])]
       [storage/media-picker ::model-thumbnail-picker
                             {:autosave?     true
                              :disabled?     viewer-disabled?
                              :indent        {:top :m :vertical :s}
                              :multi-select? false
                              :placeholder   "n/a"
                              :value-path    [:vehicle-models :viewer/viewed-item :thumbnail]

                              ; TEMP#0051 (source-code/app/common/item_picker/views.cljs)
                              :read-only? true}]))

(defn- model-thumbnail-box
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :vehicle-models.viewer])]
       [components/surface-box ::model-thumbnail-box
                               {:indent  {:top :m}
                                :content [:<> [model-thumbnail-picker]
                                              [elements/horizontal-separator {:height :s}]]
                                :disabled? viewer-disabled?
                                :label     :thumbnail}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- model-tags
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :vehicle-models.viewer])]
       [elements/chip-group {:disabled?           viewer-disabled?
                             :horizontal-position :left
                             :indent              {:top :m :vertical :s}
                             :label               :tags
                             :placeholder         "n/a"
                             :value-path          [:vehicle-models :viewer/viewed-item :tags]}]))

(defn- model-description
  []
  (let [viewer-disabled?  @(r/subscribe [:item-viewer/viewer-disabled? :vehicle-models.viewer])
        model-description @(r/subscribe [:x.db/get-item [:vehicle-models :viewer/viewed-item :description]])]
       [components/data-element ::model-description
                                {:disabled? viewer-disabled?
                                 :indent      {:top :m :vertical :s}
                                 :label       :description
                                 :placeholder "n/a"
                                 :value       model-description}]))

(defn- model-product-description
  []
  (let [viewer-disabled?          @(r/subscribe [:item-viewer/viewer-disabled? :vehicle-models.viewer])
        model-product-description @(r/subscribe [:x.db/get-item [:vehicle-models :viewer/viewed-item :product-description]])]
       [components/data-element ::model-product-description
                                {:disabled?   viewer-disabled?
                                 :indent      {:top :m :vertical :s}
                                 :label       :product-description
                                 :placeholder "n/a"
                                 :value       model-product-description}]))

(defn- model-name
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :vehicle-models.viewer])
        model-name       @(r/subscribe [:x.db/get-item [:vehicle-models :viewer/viewed-item :name]])]
       [components/data-element ::model-name
                                {:disabled?   viewer-disabled?
                                 :indent      {:top :m :vertical :s}
                                 :label       :name
                                 :placeholder "n/a"
                                 :value       model-name}]))

(defn- model-type-count
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :vehicle-models.viewer])
        model-types      @(r/subscribe [:x.db/get-item [:vehicle-models :viewer/viewed-item :types]])
        model-type-count  (count model-types)]
       [components/data-element ::model-type-count
                                {:disabled? viewer-disabled?
                                 :indent    {:top :m :vertical :s}
                                 :label     :vehicle-type-count
                                 :value     (str model-type-count)}]))

(defn- model-data-box
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :vehicle-models.viewer])]
       [components/surface-box ::model-data-box
                               {:content [:<> [:div (forms/form-row-attributes)
                                                    [:div (forms/form-block-attributes {:ratio 33})
                                                          [model-name]]
                                                    [:div (forms/form-block-attributes {:ratio 33})
                                                          [model-product-description]]
                                                    [:div (forms/form-block-attributes {:ratio 34})
                                                          [model-type-count]]]
                                              [:div (forms/form-row-attributes)
                                                    [:div (forms/form-block-attributes {:ratio 100})
                                                          [model-tags]]]
                                              [:div (forms/form-row-attributes)
                                                    [:div (forms/form-block-attributes {:ratio 100})
                                                          [model-description]]]
                                              [elements/horizontal-separator {:height :s}]]
                                :disabled? viewer-disabled?
                                :label     :data}]))
