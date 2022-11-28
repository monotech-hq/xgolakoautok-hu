
(ns app.vehicle-types.frontend.editor.boxes
    (:require [app.common.frontend.api     :as common]
              [app.components.frontend.api :as components]
              [app.schemes.frontend.api    :as schemes]
              [app.storage.frontend.api    :as storage]
              [elements.api                :as elements]
              [forms.api                   :as forms]
              [re-frame.api                :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- type-scheme-form
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :vehicle-types.editor])]
       [schemes/scheme-form :vehicle-types.technical-data
                            {:disabled?        editor-disabled?
                             :suggestions-path [:vehicle-types :editor/suggestions]
                             :value-path       [:vehicle-types :editor/edited-item]}]))

(defn- type-technical-data-box
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :vehicle-types.editor])]
       [components/surface-box ::type-technical-data-box
                               {:content [:<> [schemes/scheme-controls :vehicle-types.technical-data {:disabled? editor-disabled?}]
                                              [elements/horizontal-separator {:height :m}]
                                              [type-scheme-form]
                                              [elements/horizontal-separator {:height :s}]]
                                :disabled? editor-disabled?
                                :label     :technical-data}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- type-image-picker
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :vehicle-types.editor])]
       [storage/media-picker ::type-image-picker
                             {:autosave?     false
                              :disabled?     editor-disabled?
                              :extensions    ["bmp" "jpg" "jpeg" "png" "webp"]
                              :indent        {:vertical :s}
                              :multi-select? true
                              :placeholder   "n/a"
                              :sortable?     true
                              :toggle-label  :select-images!
                              :value-path    [:vehicle-types :editor/edited-item :images]}]))

(defn- type-images-box
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :vehicle-types.editor])]
       [components/surface-box ::type-images-box
                               {:content [:<> [:div (forms/form-row-attributes)
                                                    [:div (forms/form-block-attributes {:ratio 100})
                                                          [type-image-picker]]]
                                              [elements/horizontal-separator {:height :s}]]
                                :disabled? editor-disabled?
                                :label     :images}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- type-file-picker
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :vehicle-types.editor])]
       [storage/media-picker ::type-file-picker
                             {:autosave?     false
                              :disabled?     editor-disabled?
                              :extensions    ["doc" "xls" "pdf"]
                              :indent        {:vertical :s}
                              :multi-select? true
                              :placeholder   "n/a"
                              :sortable?     true
                              :toggle-label  :select-files!
                              :value-path    [:vehicle-types :editor/edited-item :files]}]))

(defn- type-files-box
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :vehicle-types.editor])]
       [components/surface-box ::type-files-box
                               {:content [:<> [:div (forms/form-row-attributes)
                                                    [:div (forms/form-block-attributes {:ratio 100})
                                                          [type-file-picker]]]
                                              [elements/horizontal-separator {:height :s}]]
                                :disabled? editor-disabled?
                                :label     :files}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- type-transport-cost-field
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :vehicle-types.editor])]
       [elements/combo-box ::type-transport-cost-field
                           {:disabled?      editor-disabled?
                            :emptiable?     false
                            :end-adornments [{:label "EUR" :color :muted}]
                            :indent         {:top :m :vertical :s}
                            :label          :transport-cost
                            :options-path   [:vehicle-types :editor/suggestions :transport-cost]
                            :value-path     [:vehicle-types :editor/edited-item :transport-cost]}]))

(defn- type-manufacturer-price-field
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :vehicle-types.editor])]
       [elements/combo-box ::type-manufacturer-price-field
                           {:disabled?      editor-disabled?
                            :emptiable?     false
                            :end-adornments [{:label "EUR" :color :muted}]
                            :indent         {:top :m :vertical :s}
                            :label          :manufacturer-price
                            :options-path   [:vehicle-types :editor/suggestions :manufacturer-price]
                            :value-path     [:vehicle-types :editor/edited-item :manufacturer-price]}]))

(defn- type-price-margin-field
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :vehicle-types.editor])]
       [elements/combo-box ::type-price-margin-field
                           {:disabled?      editor-disabled?
                            :emptiable?     false
                            :end-adornments [{:label "%" :color :muted}]
                            :indent         {:top :m :vertical :s}
                            :label          :price-margin
                            :options-path   [:vehicle-types :editor/suggestions :price-margin]
                            :value-path     [:vehicle-types :editor/edited-item :price-margin]}]))

(defn- type-dealer-rebate-field
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :vehicle-types.editor])]
       [elements/combo-box ::type-dealer-rebate-field
                           {:disabled?      editor-disabled?
                            :emptiable?     false
                            :end-adornments [{:label "%" :color :muted}]
                            :indent         {:top :m :vertical :s}
                            :label          :dealer-rebate
                            :options-path   [:vehicle-types :editor/suggestions :dealer-rebate]
                            :value-path     [:vehicle-types :editor/edited-item :dealer-rebate]}]))

(defn- type-price-box
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :vehicle-types.editor])]
       [components/surface-box ::type-price-box
                               {:content [:<> [:div (forms/form-row-attributes)
                                                    [:div (forms/form-block-attributes {:ratio 33})
                                                          [type-manufacturer-price-field]]
                                                    [:div (forms/form-block-attributes {:ratio 33})
                                                          [type-price-margin-field]]
                                                    [:div (forms/form-block-attributes {:ratio 34})
                                                          [type-dealer-rebate-field]]]
                                              [:div (forms/form-row-attributes)
                                                    [:div (forms/form-block-attributes {:ratio 33})
                                                          [type-transport-cost-field]]
                                                    [:div (forms/form-block-attributes {:ratio 33})]
                                                    [:div (forms/form-block-attributes {:ratio 34})]]
                                              [elements/horizontal-separator {:height :s}]]
                                :disabled? editor-disabled?
                                :label     :price}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- type-name-field
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :vehicle-types.editor])]
       [elements/combo-box ::type-name-field
                           {:autofocus?   true
                            :disabled?    editor-disabled?
                            :emptiable?   false
                            :indent       {:top :m :vertical :s}
                            :label        :name
                            :options-path [:vehicle-types :editor/suggestions :name]
                            :placeholder  :vehicle-type-name-placeholder
                            :required?    true
                            :value-path   [:vehicle-types :editor/edited-item :name]}]))

(defn- type-basic-data
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :vehicle-types.editor])]
       [components/surface-box ::type-basic-data
                               {:content [:<> [:div (forms/form-row-attributes)
                                                    [:div (forms/form-block-attributes {:ratio 100})
                                                          [type-name-field]]]
                                              [elements/horizontal-separator {:height :s}]]
                                :disabled? editor-disabled?
                                :label     :basic-data}]))
