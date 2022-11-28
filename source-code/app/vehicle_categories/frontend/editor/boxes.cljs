
(ns app.vehicle-categories.frontend.editor.boxes
    (:require [app.common.frontend.api         :as common]
              [app.components.frontend.api     :as components]
              [app.storage.frontend.api        :as storage]
              [app.vehicle-models.frontend.api :as vehicle-models]
              [elements.api                    :as elements]
              [forms.api                       :as forms]
              [re-frame.api                    :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- category-public-link
  []
  (let [editor-disabled?     @(r/subscribe [:item-editor/editor-disabled? :vehicle-categories.editor])
        category-name        @(r/subscribe [:x.db/get-item [:vehicle-categories :editor/edited-item :name]])
        category-public-link @(r/subscribe [:x.router/get-page-public-link category-name])]
       [components/data-element ::category-public-link
                                {:disabled? editor-disabled?
                                 :indent    {:top :m :vertical :s}
                                 :label     :public-link
                                 :value     category-public-link}]))

(defn- category-visibility-radio-button
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :vehicle-categories.editor])]
       [elements/radio-button ::category-visibility-radio-button
                              {:disabled?       editor-disabled?
                               :indent          {:top :m :vertical :s}
                               :label           :visibility-on-the-website
                               :options         [{:label :public-content  :helper :visible-to-everyone     :value :public}
                                                 {:label :private-content :helper :only-visible-to-editors :value :private}]
                               :option-helper-f :helper
                               :option-label-f  :label
                               :option-value-f  :value
                               :value-path      [:vehicle-categories :editor/edited-item :visibility]}]))

(defn- category-settings-box
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :vehicle-categories.editor])]
       [components/surface-box ::category-settings-box
                               {:content [:<> [:div (forms/form-row-attributes)
                                                    [:div (forms/form-block-attributes {:ratio 100})
                                                          [category-visibility-radio-button]]]
                                              [:div (forms/form-row-attributes)
                                                    [:div (forms/form-block-attributes {:ratio 100})
                                                          [category-public-link]]]
                                              [elements/horizontal-separator {:height :s}]]
                                :disabled? editor-disabled?
                                :label     :settings}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- category-vehicle-model-picker
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :vehicle-categories.editor])]
       [vehicle-models/model-picker ::category-vehicle-model-picker
                                    {:autosave?     false
                                     :disabled?     editor-disabled?
                                     :indent        {:vertical :s}
                                     :multi-select? true
                                     :placeholder   "n/a"
                                     :sortable?     true
                                     :value-path    [:vehicle-categories :editor/edited-item :models]}]))

(defn- category-vehicle-models-box
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :vehicle-categories.editor])]
       [components/surface-box ::category-vehicle-models-box
                               {:content [:<> [:div (forms/form-row-attributes)
                                                    [:div (forms/form-block-attributes {:ratio 100})
                                                          [category-vehicle-model-picker]]]
                                              [elements/horizontal-separator {:height :s}]]
                                :disabled? editor-disabled?
                                :label     :vehicle-models}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- category-thumbnail-picker
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :vehicle-categories.editor])]
       [storage/media-picker ::category-thumbnail-picker
                             {:autosave?     true
                              :disabled?     editor-disabled?
                              :extensions    ["bmp" "jpg" "jpeg" "png" "webp"]
                              :indent        {:vertical :s}
                              :multi-select? false
                              :placeholder   "n/a"
                              :toggle-label  :select-image!
                              :value-path    [:vehicle-categories :editor/edited-item :thumbnail]}]))

(defn- category-thumbnail-box
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :vehicle-categories.editor])]
       [components/surface-box ::category-thumbnail-box
                               {:content [:<> [:div (forms/form-row-attributes)
                                                    [:div (forms/form-block-attributes {:ratio 100})
                                                          [category-thumbnail-picker]]]
                                              [elements/horizontal-separator {:height :s}]]
                                :disabled? editor-disabled?
                                :label     :thumbnail}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- category-description-field
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :vehicle-categories.editor])]
       [elements/multiline-field ::category-description-field
                                 {:disabled?   editor-disabled?
                                  :emptiable?  false
                                  :indent      {:top :m :vertical :s}
                                  :label       :description
                                  :placeholder :vehicle-category-description-placeholder
                                  :value-path  [:vehicle-categories :editor/edited-item :description]}]))

(defn- category-name-field
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :vehicle-categories.editor])]
       [elements/combo-box ::category-name-field
                           {:autofocus?   true
                            :disabled?    editor-disabled?
                            :emptiable?   false
                            :indent       {:top :m :vertical :s}
                            :label        :name
                            :options-path [:vehicle-categories :editor/suggestions :name]
                            :placeholder  :vehicle-category-name-placeholder
                            :required?    true
                            :value-path   [:vehicle-categories :editor/edited-item :name]}]))

(defn- category-data-box
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :vehicle-categories.editor])]
       [components/surface-box ::category-data-box
                               {:content [:<> [:div (forms/form-row-attributes)
                                                    [:div (forms/form-block-attributes {:ratio 100})
                                                          [category-name-field]]]
                                              [:div (forms/form-row-attributes)
                                                    [:div (forms/form-block-attributes {:ratio 100})
                                                          [category-description-field]]]
                                              [elements/horizontal-separator {:height :s}]]
                                :disabled? editor-disabled?
                                :label     :data}]))
