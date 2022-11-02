
(ns app.products.frontend.editor.views
    (:require [app.common.frontend.api  :as common]
              [app.storage.frontend.api :as storage]
              [elements.api             :as elements]
              [engines.item-editor.api  :as item-editor]
              [forms.api                :as forms]
              [layouts.surface-a.api    :as surface-a]
              [re-frame.api             :as r]))

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
                              :placeholder   "-"
                              :sortable?     true
                              :toggle-label  :select-images!
                              :thumbnail     {:height :3xl :width :5xl}
                              :value-path    [:products :editor/edited-item :images]}]))

(defn- product-images-box
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :products.editor])]
       [common/surface-box ::product-images-box
                           {:content [:<> [product-image-picker]
                                          [elements/horizontal-separator {:size :s}]]
                            :disabled? editor-disabled?
                            :label     :images}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- product-images
  []
  [:<> [product-images-box]])

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
                              :placeholder   "-"
                              :toggle-label  :select-image!
                              :thumbnail     {:height :3xl :width :5xl}
                              :value-path    [:products :editor/edited-item :thumbnail]}]))

(defn- product-thumbnail-box
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :products.editor])]
       [common/surface-box ::product-thumbnail-box
                           {:content [:<> [product-thumbnail-picker]
                                          [elements/horizontal-separator {:size :s}]]
                            :disabled? editor-disabled?
                            :label     :thumbnail}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- product-thumbnail
  []
  [:<> [product-thumbnail-box]])

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
                             :end-adornments [{:label "EUR"}]
                             :indent         {:top :m :vertical :s}
                             :label          :price
                             :placeholder    "0"
                             :value-path     [:products :editor/edited-item :price]}]))

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
       [common/surface-box ::product-data-box
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
                                          [elements/horizontal-separator {:size :s}]]
                            :disabled? editor-disabled?
                            :label     :data}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- product-data
  []
  [:<> [product-data-box]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- body
  []
  (let [current-view-id @(r/subscribe [:gestures/get-current-view-id :products.editor])]
       (case current-view-id :data      [product-data]
                             :thumbnail [product-thumbnail]
                             :images    [product-images])))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- menu-bar
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :products.editor])]
       [common/item-editor-menu-bar :products.editor
                                    {:disabled?  editor-disabled?
                                     :menu-items [{:label :data      :change-keys [:name :description :price :quantity-unit :item-number]}
                                                  {:label :thumbnail :change-keys [:thumbnail]}
                                                  {:label :images    :change-keys [:images]}]}]))

(defn- controls
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :products.editor])]
       [common/item-editor-controls :products.editor
                                    {:disabled? editor-disabled?}]))

(defn- breadcrumbs
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :products.editor])
        product-name        @(r/subscribe [:db/get-item [:products :editor/edited-item :name]])
        product-id          @(r/subscribe [:router/get-current-route-path-param :item-id])
        product-uri          (str "/@app-home/products/" product-id)]
       [common/surface-breadcrumbs :products.editor/view
                                   {:crumbs (if product-id [{:label :app-home    :route "/@app-home"}
                                                            {:label :products    :route "/@app-home/products"}
                                                            {:label product-name :route product-uri :placeholder :unnamed-product}
                                                            {:label :edit!}]
                                                           [{:label :app-home :route "/@app-home"}
                                                            {:label :products :route "/@app-home/products"}
                                                            {:label :add!}])
                                    :disabled? editor-disabled?}]))

(defn- label
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :products.editor])
        product-name        @(r/subscribe [:db/get-item [:products :editor/edited-item :name]])]
       [common/surface-label :products.editor/view
                             {:disabled?   editor-disabled?
                              :label       product-name
                              :placeholder :unnamed-product}]))

(defn- header
  []
  [:<> [:div {:style {:display "flex" :justify-content "space-between" :flex-wrap "wrap" :grid-row-gap "48px"}}
             [:div [label]
                   [breadcrumbs]]
             [:div [controls]]]
       [elements/horizontal-separator {:size :xxl}]
       [menu-bar]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-structure
  []
  [:<> [header]
       [body]])

(defn- product-editor
  []
  [item-editor/body :products.editor
                    {:auto-title?      true
                     :form-element     #'view-structure
                     :error-element    [common/error-content {:error :the-item-you-opened-may-be-broken}]
                     :ghost-element    #'common/item-editor-ghost-element
                     :initial-item     {:quantity-unit {:label :piece :value :n-pieces}}
                     :item-path        [:products :editor/edited-item]
                     :label-key        :name
                     :suggestion-keys  [:name]
                     :suggestions-path [:products :editor/suggestions]}])

(defn view
  [surface-id]
  [surface-a/layout surface-id
                    {:content #'product-editor}])
