
(ns app.products.frontend.selector.views
    (:require [app.common.frontend.api     :as common]
              [app.components.frontend.api :as components]
              [elements.api                :as elements]
              [layouts.popup-a.api         :as popup-a]
              [re-frame.api                :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- footer
  ; @param (keyword) popup-id
  [_]
  (let [selected-product-count @(r/subscribe [:item-lister/get-selected-item-count :products.selector])
        on-discard-selection [:item-lister/discard-selection! :products.selector]]
       [common/item-selector-footer :products.selector
                                    {:on-discard-selection on-discard-selection
                                     :selected-item-count  selected-product-count}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- product-list-item
  ; @param (keyword) selector-id
  ; @param (map) selector-props
  ;  {:countable? (boolean)(opt)}
  ; @param (integer) item-dex
  ; @param (map) product-item
  [selector-id {:keys [countable?]} item-dex {:keys [id item-number name quantity-unit thumbnail]}]
  (let [product-count @(r/subscribe [:item-selector/get-item-count selector-id id])
        product-count  {:content (:value quantity-unit) :replacements [product-count]}
        item-number    {:content :item-number-n :replacements [(or item-number "n/a")]}]
       [components/item-list-row {:cells [[components/list-item-gap       {:width 12}]
                                          (if countable? [common/selector-item-counter selector-id item-dex {:item-id id}])
                                          (if countable? [components/list-item-gap {:width 12}])
                                          [components/list-item-thumbnail {:thumbnail (:media/uri thumbnail)}]
                                          [components/list-item-gap       {:width 12}]
                                          [components/list-item-cell      {:rows [{:content name :placeholder :unnamed-product}
                                                                                  {:content item-number :font-size :xs :color :muted}
                                                                                  (if countable? {:content product-count :font-size :xs :color :muted})]}]
                                          [components/list-item-gap {:width 6}]
                                          [common/selector-item-marker selector-id item-dex {:item-id id}]
                                          [components/list-item-gap {:width 6}]]
                                  :border (if (not= item-dex 0) :top)}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- body
  ; @param (keyword) popup-id
  [_]
  [:<> [elements/horizontal-separator {:height :xs}]
       [common/item-selector-body :products.selector
                                  {:items-path        [:products :selector/downloaded-items]
                                   :list-item-element #'product-list-item}]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- control-bar
  []
  (let [selector-disabled? @(r/subscribe [:item-lister/lister-disabled? :products.selector])]
       [common/item-selector-control-bar :products.selector
                                         {:disabled?                selector-disabled?
                                          :search-field-placeholder :search-in-products
                                          :search-keys              [:item-number :name]}]))

(defn- label-bar
  []
  (let [multi-select? @(r/subscribe [:item-lister/get-meta-item :products.selector :multi-select?])]
       [common/item-selector-label-bar :products.selector
                                       {:label    (if multi-select? :select-products! :select-product!)
                                        :on-close [:x.ui/remove-popup! :products.selector/view]}]))

(defn- header
  ; @param (keyword) popup-id
  [_]
  [:<> [label-bar]
       [control-bar]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn view
  ; @param (keyword) popup-id
  [popup-id]
  [popup-a/layout popup-id
                  {:footer              #'footer
                   :body                #'body
                   :header              #'header
                   :min-width           :m
                   :stretch-orientation :vertical}])
