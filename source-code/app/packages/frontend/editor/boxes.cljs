
(ns app.packages.frontend.editor.boxes
    (:require [app.common.frontend.api     :as common]
              [app.components.frontend.api :as components]
              [app.products.frontend.api   :as products]
              [app.services.frontend.api   :as services]
              [app.storage.frontend.api    :as storage]
              [elements.api                :as elements]
              [forms.api                   :as forms]
              [re-frame.api                :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- package-automatic-pricing-checkbox
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :packages.editor])]
       [elements/checkbox ::package-automatic-pricing-checkbox
                          {:disabled?       editor-disabled?
                           :indent          {:top :m :vertical :s}
                           :options         [{:label :automatic-pricing :helper :package-automatic-pricing-helper :value true}]
                           :option-helper-f :helper
                           :option-label-f  :label
                           :option-value-f  :value
                           :value-path      [:packages :editor/edited-item :automatic-pricing?]}]))

(defn- package-settings-box
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :packages.editor])]
       [components/surface-box ::package-settings-box
                               {:content [:<> [:div (forms/form-row-attributes)
                                                    [:div (forms/form-block-attributes {:ratio 100})
                                                          [package-automatic-pricing-checkbox]]]
                                              [elements/horizontal-separator {:height :s}]]
                                :disabled? editor-disabled?
                                :label     :settings}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- package-product-picker
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :packages.editor])]
       [products/product-picker ::package-product-picker
                                {:autosave?     false
                                 :disabled?     editor-disabled?
                                 :indent        {:vertical :s}
                                 :multi-select? true
                                 :placeholder   "n/a"
                                 :sortable?     true
                                 :value-path    [:packages :editor/edited-item :products]}]))

(defn- package-products-box
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :packages.editor])]
       [components/surface-box ::package-products-box
                               {:content [:<> [:div (forms/form-row-attributes)
                                                    [:div (forms/form-block-attributes {:ratio 100})
                                                          [package-product-picker]]]
                                              [elements/horizontal-separator {:height :s}]]
                                :disabled? editor-disabled?
                                :label     :products}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- package-service-picker
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :packages.editor])]
       [services/service-picker ::package-service-picker
                                {:autosave?     false
                                 :disabled?     editor-disabled?
                                 :indent        {:vertical :s}
                                 :multi-select? true
                                 :placeholder   "n/a"
                                 :value-path    [:packages :editor/edited-item :services]}]))

(defn- package-services-box
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :packages.editor])]
       [components/surface-box ::package-services-box
                               {:content [:<> [:div (forms/form-row-attributes)
                                                    [:div (forms/form-block-attributes {:ratio 100})
                                                          [package-service-picker]]]
                                              [elements/horizontal-separator {:height :s}]]
                                :disabled? editor-disabled?
                                :label     :services}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- package-thumbnail-picker
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :packages.editor])]
       [storage/media-picker ::package-thumbnail-picker
                             {:autosave?     true
                              :disabled?     editor-disabled?
                              :extensions    ["bmp" "jpg" "jpeg" "png" "webp"]
                              :indent        {:vertical :s}
                              :multi-select? false
                              :placeholder   "n/a"
                              :toggle-label  :select-image!
                              :value-path    [:packages :editor/edited-item :thumbnail]}]))

(defn- package-thumbnail-box
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :packages.editor])]
       [components/surface-box ::package-thumbnail-box
                               {:content [:<> [:div (forms/form-row-attributes)
                                                    [:div (forms/form-block-attributes {:ratio 100})
                                                          [package-thumbnail-picker]]]
                                              [elements/horizontal-separator {:height :s}]]
                                :disabled? editor-disabled?
                                :label     :thumbnail}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- package-quantity-unit-select
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :packages.editor])]
       [elements/select ::package-quantity-unit-select
                        {:disabled?      editor-disabled?
                         :indent         {:top :m :vertical :s}
                         :option-label-f :label
                         :options        [{:label :unit       :value :n-units}
                                          {:label :piece      :value :n-pieces}
                                          {:label :minute     :value :n-mins}
                                          {:label :hour       :value :n-hours}
                                          {:label :day        :value :n-days}
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
                         :value-path     [:packages :editor/edited-item :quantity-unit]}]))

(defn- package-item-number-field
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :packages.editor])]
       [elements/text-field ::package-item-number-field
                            {:disabled?   editor-disabled?
                             :indent      {:top :m :vertical :s}
                             :label       :item-number
                             :placeholder "xxx-xxx"
                             :value-path  [:packages :editor/edited-item :item-number]}]))

(defn- package-automatic-price
  []
  (let [editor-disabled?        @(r/subscribe [:item-editor/editor-disabled? :packages.editor])
        package-automatic-price @(r/subscribe [:x.db/get-item [:packages :editor/edited-item :automatic-price]])]
       [components/data-element ::package-automatic-price
                                {:disabled?   editor-disabled?
                                 :indent      {:top :m :vertical :s}
                                 :label       :unit-price
                                 :placeholder "n/a"
                                 :value       {:content package-automatic-price :suffix " EUR"}}]))

(defn- package-price-field
  []
  (let [editor-disabled?        @(r/subscribe [:item-editor/editor-disabled? :packages.editor])
        package-automatic-price @(r/subscribe [:x.db/get-item [:packages :editor/edited-item :automatic-price]])]
       [elements/text-field ::package-price-field
                            {:disabled?      editor-disabled?
                             :end-adornments [{:label "EUR" :color :muted}]
                             :indent         {:top :m :vertical :s}
                             :label          :unit-price
                             :placeholder    package-automatic-price
                             :required?      true
                             :value-path     [:packages :editor/edited-item :unit-price]}]))

(defn- package-price
  []
  (if-let [automatic-pricing? @(r/subscribe [:x.db/get-item [:packages :editor/edited-item :automatic-pricing?]])]
          [package-automatic-price]
          [package-price-field]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- package-name-field
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :packages.editor])]
       [elements/combo-box ::package-name-field
                           {:autofocus?   true
                            :disabled?    editor-disabled?
                            :emptiable?   false
                            :indent       {:top :m :vertical :s}
                            :label        :name
                            :options-path [:packages :editor/suggestions :name]
                            :placeholder  :package-name-placeholder
                            :required?    true
                            :value-path   [:packages :editor/edited-item :name]}]))

(defn- package-description-field
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :packages.editor])]
       [elements/multiline-field ::package-description-field
                                 {:disabled?   editor-disabled?
                                  :indent      {:top :m :vertical :s}
                                  :label       :description
                                  :placeholder :package-description-placeholder
                                  :value-path  [:packages :editor/edited-item :description]}]))

(defn- package-data-box
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :packages.editor])]
       [components/surface-box ::package-data-box
                               {:content [:<> [:div (forms/form-row-attributes)
                                                    [:div (forms/form-block-attributes {:ratio 66})
                                                          [package-name-field]]
                                                    [:div (forms/form-block-attributes {:ratio 34})]]
                                              [:div (forms/form-row-attributes)
                                                    [:div (forms/form-block-attributes {:ratio 33})
                                                          [package-quantity-unit-select]]
                                                    [:div (forms/form-block-attributes {:ratio 33})
                                                          [package-price]]
                                                    [:div (forms/form-block-attributes {:ratio 34})
                                                          [package-item-number-field]]]
                                              [:div (forms/form-row-attributes)
                                                    [:div (forms/form-block-attributes {:ratio 100})
                                                          [package-description-field]]]
                                              [elements/horizontal-separator {:height :s}]]
                                :disabled? editor-disabled?
                                :label     :data}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------
