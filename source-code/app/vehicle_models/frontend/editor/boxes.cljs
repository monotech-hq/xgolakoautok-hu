
(ns app.vehicle-models.frontend.editor.boxes
    (:require [app.common.frontend.api     :as common]
              [app.components.frontend.api :as components]
              [app.storage.frontend.api    :as storage]
              [elements.api                :as elements]
              [forms.api                   :as forms]
              [re-frame.api                :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; meta-keywords    = tags
; meta-description = description

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- model-vehicle-types-helper
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :vehicle-models.editor])]
       [elements/text ::model-vehicle-types-helper
                      {:disabled?        editor-disabled?
                       :color            :muted
                       :content          "A járműtípusokat a modell adatainak mentése után tudod létrehozni és módosítani"
                       :font-size        :s
                       :font-weight      :bold
                       :horizontal-align :center
                       :indent           {:top :m :vertical :s}}]))

(defn- model-vehicle-types-box
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :vehicle-models.editor])]
       [components/surface-box ::model-vehicle-types-box
                               {:content [:<> [:div (forms/form-row-attributes)
                                                    [:div (forms/form-block-attributes {:ratio 100})
                                                          [model-vehicle-types-helper]]]
                                              [elements/horizontal-separator {:height :s}]]
                                :disabled? editor-disabled?
                                :label     :vehicle-types}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- model-thumbnail-picker
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :vehicle-models.editor])]
       [storage/media-picker ::model-thumbnail-picker
                             {:autosave?     true
                              :disabled?     editor-disabled?
                              :extensions    ["bmp" "jpg" "jpeg" "png" "webp"]
                              :indent        {:vertical :s}
                              :multi-select? false
                              :placeholder   "n/a"
                              :toggle-label  :select-image!
                              :value-path    [:vehicle-models :editor/edited-item :thumbnail]}]))

(defn- model-thumbnail-box
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :vehicle-models.editor])]
       [components/surface-box ::model-thumbnail-box
                               {:content [:<> [:div (forms/form-row-attributes)
                                                    [:div (forms/form-block-attributes {:ratio 100})
                                                          [model-thumbnail-picker]]]
                                              [elements/horizontal-separator {:height :s}]]
                                :disabled? editor-disabled?
                                :label     :thumbnail}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- model-description-field
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :vehicle-models.editor])]
       [elements/multiline-field ::model-description-field
                                 {:disabled?   editor-disabled?
                                  :indent      {:top :m :vertical :s}
                                  :label       :description
                                  :placeholder :vehicle-model-description-placeholder
                                  :value-path  [:vehicle-models :editor/edited-item :description]}]))

(defn- model-tags-field
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :vehicle-models.editor])]
       [elements/multi-combo-box ::model-tags-field
                                 {:disabled?    editor-disabled?
                                  :indent       {:top :m :vertical :s}
                                  :label        :tags
                                  :options-path [:vehicle-models :editor/suggestions :tags]
                                  :placeholder  :vehicle-model-tags-placeholder
                                  :value-path   [:vehicle-models :editor/edited-item :tags]}]))

(defn- model-name-field
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :vehicle-models.editor])]
       [elements/combo-box ::model-name-field
                           {:autofocus?   true
                            :disabled?    editor-disabled?
                            :emptiable?   false
                            :indent       {:top :m :vertical :s}
                            :label        :name
                            :options-path [:vehicle-models :editor/suggestions :name]
                            :placeholder  :vehicle-model-name-placeholder
                            :required?    true
                            :value-path   [:vehicle-models :editor/edited-item :name]}]))

(defn- model-product-description-field
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :vehicle-models.editor])]
       [elements/combo-box ::model-product-description-field
                           {:disabled?    editor-disabled?
                            :emptiable?   false
                            :indent       {:top :m :vertical :s}
                            :label        :product-description
                            :options-path [:vehicle-models :editor/suggestions :product-description]
                            :placeholder  :vehicle-model-product-description-placeholder
                            :required?    true
                            :value-path   [:vehicle-models :editor/edited-item :product-description]}]))

(defn- model-data-box
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :vehicle-models.editor])]
       [components/surface-box ::model-data-box
                               {:content [:<> [:div (forms/form-row-attributes)
                                                    [:div (forms/form-block-attributes {:ratio 100})
                                                          [model-name-field]]]
                                              [:div (forms/form-row-attributes)
                                                    [:div (forms/form-block-attributes {:ratio 100})
                                                          [model-product-description-field]]]
                                              [:div (forms/form-row-attributes)
                                                    [:div (forms/form-block-attributes {:ratio 100})
                                                          [model-tags-field]]]
                                              [:div (forms/form-row-attributes)
                                                    [:div (forms/form-block-attributes {:ratio 100})
                                                          [model-description-field]]]
                                              [elements/horizontal-separator {:height :s}]]
                                :disabled? editor-disabled?
                                :label     :data}]))
