
(ns app.rental-vehicles.frontend.editor.boxes
    (:require [app.common.frontend.api     :as common]
              [app.components.frontend.api :as components]
              [app.contents.frontend.api   :as contents]
              [app.storage.frontend.api    :as storage]
              [elements.api                :as elements]
              [forms.api                   :as forms]
              [re-frame.api                :as r]
              [time.api                    :as time]))

;; -----------------------------------------------------------------------------
;; -----------------------------------------------------------------------------

(defn- vehicle-public-link
  []
  (let [editor-disabled?    @(r/subscribe [:item-editor/editor-disabled? :rental-vehicles.editor])
        vehicle-name        @(r/subscribe [:x.db/get-item [:rental-vehicles :editor/edited-item :name]])
        vehicle-public-link @(r/subscribe [:x.router/get-page-public-link vehicle-name])]
       [components/data-element ::vehicle-public-link
                                {:disabled? editor-disabled?
                                 :indent    {:top :m :vertical :s}
                                 :label     :public-link
                                 :value     vehicle-public-link}]))

(defn- vehicle-visibility-radio-button
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :rental-vehicles.editor])]
       [elements/radio-button ::vehicle-visibility-radio-button
                              {:disabled?       editor-disabled?
                               :indent          {:top :m :vertical :s}
                               :label           :visibility-on-the-website
                               :options         [{:label :public-content  :helper :visible-to-everyone     :value :public}
                                                 {:label :private-content :helper :only-visible-to-editors :value :private}]
                               :option-helper-f :helper
                               :option-label-f  :label
                               :option-value-f  :value
                               :value-path      [:rental-vehicles :editor/edited-item :visibility]}]))

(defn- vehicle-settings-box
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :rental-vehicles.editor])]
       [components/surface-box ::vehicle-settings-box
                               {:content [:<> [:div (forms/form-row-attributes)
                                                    [:div (forms/form-block-attributes {:ratio 100})
                                                          [vehicle-visibility-radio-button]]]
                                              [:div (forms/form-row-attributes)
                                                    [:div (forms/form-block-attributes {:ratio 100})
                                                          [vehicle-public-link]]]
                                              [elements/horizontal-separator {:height :s}]]
                                :disabled? editor-disabled?
                                :label     :settings}]))

;; -----------------------------------------------------------------------------
;; -----------------------------------------------------------------------------

(defn- vehicle-description-picker
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :rental-vehicles.editor])]
       [contents/content-picker ::vehicle-description-picker
                                {:autosave?     true
                                 :color         :muted
                                 :disabled?     editor-disabled?
                                 :indent        {:vertical :s}
                                 :multi-select? false
                                 :placeholder   "n/a"
                                 :value-path    [:rental-vehicles :editor/edited-item :description]}]))

(defn- vehicle-description-box
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :rental-vehicles.editor])]
       [components/surface-box ::vehicle-description-box
                               {:content [:<> [:div (forms/form-row-attributes)
                                                    [:div (forms/form-block-attributes {:ratio 100})
                                                          [vehicle-description-picker]]]
                                              [elements/horizontal-separator {:height :s}]]
                                :disabled? editor-disabled?
                                :label     :description}]))

;; -----------------------------------------------------------------------------
;; -----------------------------------------------------------------------------

(defn- vehicle-construction-year-field
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :rental-vehicles.editor])
        server-year      @(r/subscribe [:x.core/get-server-year])]
       [elements/text-field ::vehicle-construction-year-field
                            {:disabled?   editor-disabled?
                             :indent      {:top :m :vertical :s}
                             :label       :construction-year
                             :placeholder server-year
                             :value-path  [:rental-vehicles :editor/edited-item :construction-year]}]))

(defn- vehicle-number-of-seats-counter
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :rental-vehicles.editor])]
       [elements/counter ::vehicle-number-of-seats-counter
                            {:disabled?  editor-disabled?
                             :indent     {:top :m :vertical :s}
                             :label      :number-of-seats
                             :min-value  0
                             :max-value  10
                             :value-path [:rental-vehicles :editor/edited-item :number-of-seats]}]))

(defn- vehicle-number-of-bunks-counter
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :rental-vehicles.editor])]
       [elements/counter ::vehicle-number-of-bunks-counter
                            {:disabled?  editor-disabled?
                             :indent     {:top :m :vertical :s}
                             :label      :number-of-bunks
                             :min-value  0
                             :max-value  10
                             :value-path [:rental-vehicles :editor/edited-item :number-of-bunks]}]))

(defn- vehicle-technical-data-box
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :rental-vehicles.editor])]
       [components/surface-box ::vehicle-technical-data-box
                               {:content [:<> [:div (forms/form-row-attributes)
                                                    [:div (forms/form-block-attributes {:ratio 40})
                                                          [vehicle-construction-year-field]]
                                                    [:div (forms/form-block-attributes {:ratio 10})]
                                                    [:div (forms/form-block-attributes {:ratio 25})
                                                          [vehicle-number-of-seats-counter]]
                                                    [:div (forms/form-block-attributes {:ratio 25})
                                                          [vehicle-number-of-bunks-counter]]]
                                              [elements/horizontal-separator {:height :s}]]
                                :disabled? editor-disabled?
                                :indent    {:top :m}
                                :label     :technical-data}]))

;; -----------------------------------------------------------------------------
;; -----------------------------------------------------------------------------

(defn- vehicle-image-picker
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :rental-vehicles.editor])]
       [storage/media-picker ::vehicle-image-picker
                             {:autosave?     false
                              :disabled?     editor-disabled?
                              :extensions    ["bmp" "jpg" "jpeg" "png" "webp"]
                              :indent        {:vertical :s}
                              :multi-select? true
                              :placeholder   "n/a"
                              :sortable?     true
                              :toggle-label  :select-images!
                              :value-path    [:rental-vehicles :editor/edited-item :images]}]))

(defn- vehicle-images-box
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :rental-vehicles.editor])]
       [components/surface-box ::vehicle-images-box
                               {:content [:<> [vehicle-image-picker]
                                              [elements/horizontal-separator {:height :s}]]
                                :disabled? editor-disabled?
                                :label     :images}]))

;; -----------------------------------------------------------------------------
;; -----------------------------------------------------------------------------

(defn- vehicle-thumbnail-picker
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :rental-vehicles.editor])]
       [storage/media-picker ::vehicle-thumbnail-picker
                             {:autosave?     true
                              :disabled?     editor-disabled?
                              :extensions    ["bmp" "jpg" "jpeg" "png" "webp"]
                              :indent        {:vertical :s}
                              :multi-select? false
                              :placeholder   "n/a"
                              :toggle-label  :select-image!
                              :value-path    [:rental-vehicles :editor/edited-item :thumbnail]}]))

(defn- vehicle-thumbnail-box
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :rental-vehicles.editor])]
       [components/surface-box ::vehicle-thumbnail-box
                               {:content [:<> [vehicle-thumbnail-picker]
                                              [elements/horizontal-separator {:height :s}]]
                                :disabled? editor-disabled?
                                :label     :thumbnail}]))

;; -----------------------------------------------------------------------------
;; -----------------------------------------------------------------------------

(defn- vehicle-name-field
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :rental-vehicles.editor])]
       [elements/combo-box ::vehicle-name-field
                           {:autofocus?   true
                            :disabled?    editor-disabled?
                            :indent       {:top :m :vertical :s}
                            :label        :name
                            :options-path [:rental-vehicles :editor/suggestions :name]
                            :placeholder  :rental-vehicle-name-placeholder
                            :value-path   [:rental-vehicles :editor/edited-item :name]}]))

(defn- vehicle-type-select
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :rental-vehicles.editor])]
       [elements/select ::vehicle-type-select
                        {:disabled?     editor-disabled?
                         :indent        {:top :m :vertical :s}
                         :label         :type
                         :layout        :select
                         :options-label :vehicle-type
                         :options       [:alcove-rv :semi-integrated-rv :van-rv :caravan :trailer]
                         :value-path    [:rental-vehicles :editor/edited-item :type]}]))

(defn- vehicle-basic-data-box
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :rental-vehicles.editor])]
       [components/surface-box ::vehicle-basic-data-box
                               {:content [:<> [:div (forms/form-row-attributes)
                                                    [:div (forms/form-block-attributes {:ratio 60})
                                                          [vehicle-name-field]]
                                                    [:div (forms/form-block-attributes {:ratio 40})
                                                          [vehicle-type-select]]]
                                              [elements/horizontal-separator {:height :s}]]
                                :disabled? editor-disabled?
                                :label     :basic-data}]))
