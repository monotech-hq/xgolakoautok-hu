
(ns app.products.frontend.editor.boxes
    (:require [app.common.frontend.api     :as common]
              [app.components.frontend.api :as components]
              [app.storage.frontend.api    :as storage]
              [elements.api                :as elements]
              [forms.api                   :as forms]
              [re-frame.api                :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- product-image-picker
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :products.editor])]
       [storage/media-picker ::product-image-picker
                             {:autosave?     false
                              :disabled?     editor-disabled?
                              :extensions    ["bmp" "jpg" "jpeg" "png" "webp"]
                              :indent        {:vertical :s}
                              :multi-select? true
                              :placeholder   "n/a"
                              :sortable?     true
                              :toggle-label  :select-images!
                              :value-path    [:products :editor/edited-item :images]}]))

(defn- product-images-box
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :products.editor])]
       [components/surface-box ::product-images-box
                               {:content [:<> [product-image-picker]
                                              [elements/horizontal-separator {:height :s}]]
                                :disabled? editor-disabled?
                                :label     :images}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- product-thumbnail-picker
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :products.editor])]
       [storage/media-picker ::product-thumbnail-picker
                             {:autosave?     true
                              :disabled?     editor-disabled?
                              :extensions    ["bmp" "jpg" "jpeg" "png" "webp"]
                              :indent        {:vertical :s}
                              :multi-select? false
                              :placeholder   "n/a"
                              :toggle-label  :select-image!
                              :value-path    [:products :editor/edited-item :thumbnail]}]))

(defn- product-thumbnail-box
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :products.editor])]
       [components/surface-box ::product-thumbnail-box
                               {:content [:<> [product-thumbnail-picker]
                                              [elements/horizontal-separator {:height :s}]]
                                :disabled? editor-disabled?
                                :label     :thumbnail}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- product-quantity-unit-select
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :products.editor])]
       [elements/select ::product-quantity-unit-select
                        {:disabled?      editor-disabled?
                         :indent         {:top :m :vertical :s}
                         :option-label-f :label
                         :options        [{:label :piece      :value :n-pieces}
                                          {:label :unit       :value :n-units}
                                          {:label :millimeter :value :n-mm}
                                          {:label :centimeter :value :n-cm}
                                          {:label :decimeter  :value :n-dm}
                                          {:label :meter      :value :n-m}
                                          {:label :kilometer  :value :n-km}
                                          {:label :milligram  :value :n-mg}
                                          {:label :gram       :value :n-gr}
                                          {:label :kilogram   :value :n-kg}
                                          {:label :ton        :value :n-t}]
                         :label          :quantity-unit
                         :value-path     [:products :editor/edited-item :quantity-unit]}]))

(defn- product-item-number-field
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :products.editor])]
       [elements/text-field ::product-item-number-field
                            {:disabled?   editor-disabled?
                             :indent      {:top :m :vertical :s}
                             :label       :item-number
                             :placeholder "xxx-xxx"
                             :value-path  [:products :editor/edited-item :item-number]}]))

(defn- product-price-field
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :products.editor])]
       [elements/text-field ::product-price-field
                            {:disabled?      editor-disabled?
                             :end-adornments [{:label "EUR" :color :muted}]
                             :indent         {:top :m :vertical :s}
                             :label          :unit-price
                             :placeholder    "0"
                             :value-path     [:products :editor/edited-item :unit-price]}]))

(defn- product-name-field
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :products.editor])]
       [elements/combo-box ::product-name-field
                           {:autofocus?   true
                            :disabled?    editor-disabled?
                            :emptiable?   false
                            :indent       {:top :m :vertical :s}
                            :label        :name
                            :options-path [:products :editor/suggestions :name]
                            :placeholder  :product-name-placeholder
                            :required?    true
                            :value-path   [:products :editor/edited-item :name]}]))

(defn- product-description-field
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :products.editor])]
       [elements/multiline-field ::product-description-field
                                 {:disabled?   editor-disabled?
                                  :indent      {:top :m :vertical :s}
                                  :label       :description
                                  :placeholder :product-description-placeholder
                                  :value-path  [:products :editor/edited-item :description]}]))

(defn- product-data-box
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :products.editor])]
       [components/surface-box ::product-data-box
                               {:content [:<> [:div (forms/form-row-attributes)
                                                    [:div (forms/form-block-attributes {:ratio 66})
                                                          [product-name-field]]
                                                    [:div (forms/form-block-attributes {:ratio 34})]]
                                              [:div (forms/form-row-attributes)
                                                    [:div (forms/form-block-attributes {:ratio 33})
                                                          [product-quantity-unit-select]]
                                                    [:div (forms/form-block-attributes {:ratio 33})
                                                          [product-price-field]]
                                                    [:div (forms/form-block-attributes {:ratio 34})
                                                          [product-item-number-field]]]
                                              [:div (forms/form-row-attributes)
                                                    [:div (forms/form-block-attributes {:ratio 100})
                                                          [product-description-field]]]
                                              [elements/horizontal-separator {:height :s}]]
                                :disabled? editor-disabled?
                                :label     :data}]))
