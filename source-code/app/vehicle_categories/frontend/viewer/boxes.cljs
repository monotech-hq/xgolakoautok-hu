
(ns app.vehicle-categories.frontend.viewer.boxes
    (:require [app.common.frontend.api         :as common]
              [app.components.frontend.api     :as components]
              [app.vehicle-models.frontend.api :as vehicle-models]
              [app.storage.frontend.api        :as storage]
              [elements.api                    :as elements]
              [forms.api                       :as forms]
              [re-frame.api                    :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- category-vehicle-model-picker
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :vehicle-categories.viewer])]
       [vehicle-models/model-picker ::category-vehicle-model-picker
                                    {:autosave?     false
                                     :disabled?     viewer-disabled?
                                     :indent        {:top :m :vertical :s}
                                     :multi-select? true
                                     :placeholder   "n/a"
                                     :sortable?     true
                                     :toggle-label  :select-vehicle-models!
                                     :value-path    [:vehicle-categories :viewer/viewed-item :models]

                                     ; TEMP#0051 (source-code/app/common/item_picker/views.cljs)
                                     :read-only? true}]))

(defn- category-vehicle-models-box
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :vehicle-categories.viewer])]
       [components/surface-box ::category-vehicle-models-box
                               {:content [:<> [:div (forms/form-row-attributes)
                                                    [:div (forms/form-block-attributes {:ratio 100})
                                                          [category-vehicle-model-picker]]]
                                              [elements/horizontal-separator {:height :s}]]
                                :disabled? viewer-disabled?
                                :label     :vehicle-models
                                :overflow  :hidden}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- category-thumbnail-picker
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :vehicle-categories.viewer])]
       [storage/media-picker ::category-thumbnail-picker
                             {:autosave?     true
                              :disabled?     viewer-disabled?
                              :indent        {:top :m :vertical :s}
                              :multi-select? false
                              :placeholder   "n/a"
                              :value-path    [:vehicle-categories :viewer/viewed-item :thumbnail]

                              ; TEMP#0051 (source-code/app/common/item_picker/views.cljs)
                              :read-only? true}]))

(defn- category-thumbnail-box
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :vehicle-categories.viewer])]
       [components/surface-box ::category-thumbnail-box
                               {:content   [:<> [category-thumbnail-picker]
                                                [elements/horizontal-separator {:height :s}]]
                                :disabled? viewer-disabled?
                                :indent    {:top :m}
                                :label     :thumbnail}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- category-visibility
  []
  (let [viewer-disabled?    @(r/subscribe [:item-viewer/viewer-disabled? :vehicle-categories.viewer])
        category-visibility @(r/subscribe [:x.db/get-item [:vehicle-categories :viewer/viewed-item :visibility]])]
       [components/data-element ::category-visibility
                                {:disabled? viewer-disabled?
                                 :indent    {:top :m :vertical :s}
                                 :label     :visibility-on-the-website
                                 :value     (case category-visibility :public :public-content :private :private-content)}]))

(defn- category-public-link
  []
  (let [viewer-disabled?     @(r/subscribe [:item-viewer/viewer-disabled? :vehicle-categories.viewer])
        category-name        @(r/subscribe [:x.db/get-item [:vehicle-categories :viewer/viewed-item :name]])
        category-public-link @(r/subscribe [:x.router/get-page-public-link category-name])]
       [components/data-element ::category-public-link
                                {:disabled? viewer-disabled?
                                 :indent    {:top :m :vertical :s}
                                 :label     :public-link
                                 :value     category-public-link}]))

(defn- category-description
  []
  (let [viewer-disabled?     @(r/subscribe [:item-viewer/viewer-disabled? :vehicle-categories.viewer])
        category-description @(r/subscribe [:x.db/get-item [:vehicle-categories :viewer/viewed-item :description]])]
       [components/data-element ::category-description
                                {:disabled?   viewer-disabled?
                                 :indent      {:top :m :vertical :s}
                                 :label       :description
                                 :placeholder "n/a"
                                 :value       category-description}]))

(defn- category-name
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :vehicle-categories.viewer])
        category-name    @(r/subscribe [:x.db/get-item [:vehicle-categories :viewer/viewed-item :name]])]
       [components/data-element ::category-name
                                {:disabled?   viewer-disabled?
                                 :indent      {:top :m :vertical :s}
                                 :label       :name
                                 :placeholder "n/a"
                                 :value       category-name}]))

(defn- category-model-count
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :vehicle-categories.viewer])
        category-models  @(r/subscribe [:x.db/get-item [:vehicle-categories :viewer/viewed-item :models]])
        category-model-count (count category-models)]
       [components/data-element ::category-model-count
                                {:disabled? viewer-disabled?
                                 :indent    {:top :m :vertical :s}
                                 :label     :vehicle-model-count
                                 :value     (str category-model-count)}]))

(defn- category-data-box
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :vehicle-categories.viewer])]
       [components/surface-box ::category-data-box
                               {:content [:<> [:div (forms/form-row-attributes)
                                                    [:div (forms/form-block-attributes {:ratio 33})
                                                          [category-name]]
                                                    [:div (forms/form-block-attributes {:ratio 33})
                                                          [category-model-count]]
                                                    [:div (forms/form-block-attributes {:ratio 34})]]
                                              [:div (forms/form-row-attributes)
                                                    [:div (forms/form-block-attributes {:ratio 100})
                                                          [category-description]]]
                                              [:div (forms/form-row-attributes)
                                                    [:div (forms/form-block-attributes {:ratio 33})
                                                          [category-visibility]]
                                                    [:div (forms/form-block-attributes {:ratio 67})
                                                          [category-public-link]]]
                                              [elements/horizontal-separator {:height :s}]]
                                :disabled? viewer-disabled?
                                :label     :data}]))
