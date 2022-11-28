
(ns app.services.frontend.editor.boxes
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
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :services.editor])]
       [storage/media-picker ::service-thumbnail-picker
                             {:autosave?     true
                              :disabled?     editor-disabled?
                              :extensions    ["bmp" "jpg" "jpeg" "png" "webp"]
                              :indent        {:vertical :s}
                              :multi-select? false
                              :placeholder   "n/a"
                              :toggle-label  :select-image!
                              :value-path    [:services :editor/edited-item :thumbnail]}]))

(defn- service-thumbnail-box
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :services.editor])]
       [components/surface-box ::service-thumbnail-box
                               {:content [:<> [service-thumbnail-picker]
                                              [elements/horizontal-separator {:height :s}]]
                                :disabled? editor-disabled?
                                :label     :thumbnail}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- service-quantity-unit-select
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :services.editor])]
       [elements/select ::service-quantity-unit-select
                        {:disabled?      editor-disabled?
                         :indent         {:top :m :vertical :s}
                         :option-label-f :label
                         :options        [{:label :unit   :value :n-units}
                                          {:label :minute :value :n-mins}
                                          {:label :hour   :value :n-hours}
                                          {:label :day    :value :n-days}]
                         :label          :quantity-unit
                         :value-path     [:services :editor/edited-item :quantity-unit]}]))

(defn- service-item-number-field
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :services.editor])]
       [elements/text-field ::service-item-number-field
                            {:disabled?   editor-disabled?
                             :indent      {:top :m :vertical :s}
                             :label       :item-number
                             :placeholder "xxx-xxx"
                             :value-path  [:services :editor/edited-item :item-number]}]))

(defn- service-price-field
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :services.editor])]
       [elements/text-field ::service-price-field
                            {:disabled?      editor-disabled?
                             :end-adornments [{:label "EUR" :color :muted}]
                             :indent         {:top :m :vertical :s}
                             :label          :unit-price
                             :placeholder    "0"
                             :value-path     [:services :editor/edited-item :unit-price]}]))

(defn- service-name-field
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :services.editor])]
       [elements/combo-box ::service-name-field
                           {:autofocus?   true
                            :disabled?    editor-disabled?
                            :emptiable?   false
                            :indent       {:top :m :vertical :s}
                            :label        :name
                            :options-path [:services :editor/suggestions :name]
                            :placeholder  :service-name-placeholder
                            :required?    true
                            :value-path   [:services :editor/edited-item :name]}]))

(defn- service-description-field
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :services.editor])]
       [elements/multiline-field ::service-description-field
                                 {:disabled?   editor-disabled?
                                  :indent      {:top :m :vertical :s}
                                  :label       :description
                                  :placeholder :service-description-placeholder
                                  :value-path  [:services :editor/edited-item :description]}]))

(defn- service-data-box
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :services.editor])]
       [components/surface-box ::service-data-box
                               {:content [:<> [:div (forms/form-row-attributes)
                                                    [:div (forms/form-block-attributes {:ratio 66})
                                                          [service-name-field]]
                                                    [:div (forms/form-block-attributes {:ratio 34})]]
                                              [:div (forms/form-row-attributes)
                                                    [:div (forms/form-block-attributes {:ratio 33})
                                                          [service-quantity-unit-select]]
                                                    [:div (forms/form-block-attributes {:ratio 33})
                                                          [service-price-field]]
                                                    [:div (forms/form-block-attributes {:ratio 34})
                                                          [service-item-number-field]]]
                                              [:div (forms/form-row-attributes)
                                                    [:div (forms/form-block-attributes {:ratio 100})
                                                          [service-description-field]]]
                                              [elements/horizontal-separator {:height :s}]]
                                :disabled? editor-disabled?
                                :label     :data}]))
