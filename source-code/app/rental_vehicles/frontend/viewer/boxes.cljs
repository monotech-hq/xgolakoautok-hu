
(ns app.rental-vehicles.frontend.viewer.boxes
    (:require [app.common.frontend.api     :as common]
              [app.components.frontend.api :as components]
              [app.contents.frontend.api   :as contents]
              [app.storage.frontend.api    :as storage]
              [elements.api                :as elements]
              [forms.api                   :as forms]
              [re-frame.api                :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- vehicle-description-preview
  []
  (let [viewer-disabled?    @(r/subscribe [:item-viewer/viewer-disabled? :rental-vehicles.viewer])
        vehicle-description @(r/subscribe [:x.db/get-item [:rental-vehicles :viewer/viewed-item :description]])]
       [contents/content-preview ::vehicle-description
                                 {:color       :muted
                                  :disabled?   viewer-disabled?
                                  :indent      {:top :m :vertical :s}
                                  :item-link   vehicle-description
                                  :placeholder "n/a"}]))

(defn- vehicle-description-box
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :rental-vehicles.viewer])]
       [components/surface-box ::vehicle-description-box
                               {:content [:<> [:div (forms/form-row-attributes)
                                                    [:div (forms/form-block-attributes {:ratio 100})
                                                          [vehicle-description-preview]]]
                                              [elements/horizontal-separator {:height :s}]]
                                :disabled? viewer-disabled?
                                :label     :description}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- vehicle-image-picker
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :rental-vehicles.viewer])]
       [storage/media-picker ::vehicle-image-picker
                             {:autosave?     false
                              :disabled?     viewer-disabled?
                              :indent        {:top :m :vertical :s}
                              :multi-select? true
                              :placeholder   "n/a"
                              :value-path    [:rental-vehicles :viewer/viewed-item :images]

                              ; TEMP#0051 (source-code/app/common/item_picker/views.cljs)
                              :read-only? true}]))

(defn- vehicle-images-box
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :rental-vehicles.viewer])]
       [components/surface-box ::vehicle-images-box
                               {:content [:<> [vehicle-image-picker]
                                              [elements/horizontal-separator {:height :s}]]
                                :disabled? viewer-disabled?
                                :label     :images}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- vehicle-thumbnail-picker
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :rental-vehicles.viewer])]
       [storage/media-picker ::vehicle-thumbnail-picker
                             {:autosave?     true
                              :disabled?     viewer-disabled?
                              :indent        {:top :m :vertical :s}
                              :multi-select? false
                              :placeholder   "n/a"
                              :value-path    [:rental-vehicles :viewer/viewed-item :thumbnail]

                              ; TEMP#0051 (source-code/app/common/item_picker/views.cljs)
                              :read-only? true}]))

(defn- vehicle-thumbnail-box
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :rental-vehicles.viewer])]
       [components/surface-box ::vehicle-thumbnail-box
                               {:content [:<> [vehicle-thumbnail-picker]
                                              [elements/horizontal-separator {:height :s}]]
                                :disabled? viewer-disabled?
                                :label     :thumbnail}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- vehicle-number-of-seats
  []
  (let [viewer-disabled?        @(r/subscribe [:item-viewer/viewer-disabled? :rental-vehicles.viewer])
        vehicle-number-of-seats @(r/subscribe [:x.db/get-item [:rental-vehicles :viewer/viewed-item :number-of-seats]])]
       [components/data-element ::vehicle-number-of-seats
                                {:indent      {:top :m :vertical :s}
                                 :label       :number-of-seats
                                 :placeholder "n/a"
                                 :value       vehicle-number-of-seats}]))

(defn- vehicle-number-of-bunks
  []
  (let [viewer-disabled?        @(r/subscribe [:item-viewer/viewer-disabled? :rental-vehicles.viewer])
        vehicle-number-of-bunks @(r/subscribe [:x.db/get-item [:rental-vehicles :viewer/viewed-item :number-of-bunks]])]
       [components/data-element ::vehicle-number-of-bunks
                                {:indent      {:top :m :vertical :s}
                                 :label       :number-of-bunks
                                 :placeholder "n/a"
                                 :value       vehicle-number-of-bunks}]))

(defn- vehicle-construction-year
  []
  (let [viewer-disabled?          @(r/subscribe [:item-viewer/viewer-disabled? :rental-vehicles.viewer])
        vehicle-construction-year @(r/subscribe [:x.db/get-item [:rental-vehicles :viewer/viewed-item :construction-year]])]
       [components/data-element ::vehicle-construction-year
                                {:indent      {:top :m :vertical :s}
                                 :label       :construction-year
                                 :placeholder "n/a"
                                 :value       vehicle-construction-year}]))

(defn- vehicle-technical-data-box
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :rental-vehicles.viewer])]
       [components/surface-box ::vehicle-technical-data-box
                               {:content [:<> [:div (forms/form-row-attributes)
                                                    [:div (forms/form-block-attributes {:ratio 33})
                                                          [vehicle-construction-year]]
                                                    [:div (forms/form-block-attributes {:ratio 33})
                                                          [vehicle-number-of-seats]]
                                                    [:div (forms/form-block-attributes {:ratio 34})
                                                          [vehicle-number-of-bunks]]]
                                              [elements/horizontal-separator {:height :s}]]
                                :disabled? viewer-disabled?
                                :indent    {:top :m}
                                :label     :technical-data}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- vehicle-visibility
  []
  (let [viewer-disabled?    @(r/subscribe [:item-viewer/viewer-disabled? :rental-vehicles.viewer])
        vehicle-visibility  @(r/subscribe [:x.db/get-item [:rental-vehicles :viewer/viewed-item :visibility]])]
       [components/data-element ::vehicle-visibility
                                {:disabled? viewer-disabled?
                                 :indent    {:top :m :vertical :s}
                                 :label     :visibility-on-the-website
                                 :value     (case vehicle-visibility :public :public-content :private :private-content)}]))

(defn- vehicle-public-link
  []
  (let [viewer-disabled?    @(r/subscribe [:item-viewer/viewer-disabled? :rental-vehicles.viewer])
        vehicle-name        @(r/subscribe [:x.db/get-item [:rental-vehicles :viewer/viewed-item :name]])
        vehicle-public-link @(r/subscribe [:x.router/get-page-public-link vehicle-name])]
       [components/data-element ::vehicle-public-link
                                {:disabled? viewer-disabled?
                                 :indent    {:top :m :vertical :s}
                                 :label     :public-link
                                 :value     vehicle-public-link}]))

(defn- vehicle-more-data-box
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :rental-vehicles.viewer])]
       [components/surface-box ::vehicle-more-data-box
                               {:content [:<> [:div (forms/form-row-attributes)
                                                    [:div (forms/form-block-attributes {:ratio 33})
                                                          [vehicle-visibility]]
                                                    [:div (forms/form-block-attributes {:ratio 67})
                                                          [vehicle-public-link]]]
                                              [elements/horizontal-separator {:height :s}]]
                                :disabled? viewer-disabled?
                                :indent    {:top :m}
                                :label     :more-data}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- vehicle-name
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :rental-vehicles.viewer])
        vehicle-name     @(r/subscribe [:x.db/get-item [:rental-vehicles :viewer/viewed-item :name]])]
       [components/data-element ::vehicle-name
                                {:indent      {:top :m :vertical :s}
                                 :label       :name
                                 :placeholder "n/a"
                                 :value       vehicle-name}]))

(defn- vehicle-type
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :rental-vehicles.viewer])
        vehicle-type     @(r/subscribe [:x.db/get-item [:rental-vehicles :viewer/viewed-item :type]])]
       [components/data-element ::vehicle-type
                                {:indent      {:top :m :vertical :s}
                                 :label       :type
                                 :placeholder "n/a"
                                 :value       vehicle-type}]))

(defn- vehicle-basic-data-box
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :rental-vehicles.viewer])]
       [components/surface-box ::vehicle-basic-data-box
                               {:content [:<> [:div (forms/form-row-attributes)
                                                    [:div (forms/form-block-attributes {:ratio 66})
                                                          [vehicle-name]]
                                                    [:div (forms/form-block-attributes {:ratio 34})
                                                          [vehicle-type]]]
                                              [elements/horizontal-separator {:height :s}]]
                                :disabled? viewer-disabled?
                                :label     :basic-data}]))
