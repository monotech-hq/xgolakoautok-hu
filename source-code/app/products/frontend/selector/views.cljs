
(ns app.products.frontend.selector.views
    (:require [app.common.frontend.api :as common]
              [elements.api            :as elements]
              [engines.item-lister.api :as item-lister]
              [layouts.popup-a.api     :as popup-a]
              [re-frame.api            :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- footer
  []
  (let [selected-product-count @(r/subscribe [:item-lister/get-selected-item-count :products.selector])
        on-discard-selection [:item-lister/discard-selection! :products.selector]]
       [common/item-selector-footer :products.selector
                                    {:on-discard-selection on-discard-selection
                                     :selected-item-count  selected-product-count}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- product-item-structure
  [selector-id item-dex {:keys [id item-number modified-at name quantity-unit thumbnail]}]
  (let [timestamp     @(r/subscribe [:x.activities/get-actual-timestamp modified-at])
        item-last?    @(r/subscribe [:item-lister/item-last? selector-id item-dex])
        product-count @(r/subscribe [:item-selector/get-item-count selector-id id])
        item-number    {:content :item-number-n :replacements [item-number]}
        item-count     {:content (:value quantity-unit) :replacements [product-count]}]
       [common/list-item-structure {:cells [[common/selector-item-counter  selector-id item-dex {:item-id id}]
                                            [common/list-item-thumbnail    {:thumbnail (:media/uri thumbnail)}]
                                            [common/list-item-cell {:rows [{:content name :placeholder :unnamed-product}
                                                                           {:content item-number :font-size :xs :color :muted}
                                                                           {:content item-count  :font-size :xs :color :muted}]
                                                                    :width :stretch}]
                                            [common/selector-item-marker   selector-id item-dex {:item-id id}]]
                                    :separator (if-not item-last? :bottom)}]))

(defn- product-item
  [selector-id item-dex {:keys [id] :as product-item}]
  [elements/toggle {:content     [product-item-structure selector-id item-dex product-item]
                    :hover-color :highlight
                    :on-click    [:item-selector/item-clicked :products.selector id]}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- product-list
  [lister-id items]
  [common/item-list lister-id {:item-element #'product-item :items items}])

(defn- product-lister
  []
  [item-lister/body :products.selector
                    {:default-order-by :modified-at/descending
                     :items-path       [:products :selector/downloaded-items]
                     :error-element    [common/error-content {:error :the-content-you-opened-may-be-broken}]
                     :ghost-element    #'common/item-selector-ghost-element
                     :list-element     #'product-list}])

(defn- body
  []
  [:<> [elements/horizontal-separator {:size :xs}]
       [product-lister]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- control-bar
  []
  (let [selector-disabled? @(r/subscribe [:item-lister/lister-disabled? :products.selector])]
       [common/item-selector-control-bar :products.selector
                                         {:disabled?        selector-disabled?
                                          :order-by-options [:modified-at/ascending :modified-at/descending :name/ascending :name/descending]
                                          :search-field-placeholder :search-in-products
                                          :search-keys      [:item-number :name]}]))

(defn- label-bar
  []
  (let [multi-select? @(r/subscribe [:item-lister/get-meta-item :products.selector :multi-select?])]
       [common/popup-label-bar :products.selector/view
                               {:primary-button   {:label :save!   :on-click [:item-selector/save-selection! :products.selector]}
                                :secondary-button {:label :cancel! :on-click [:x.ui/remove-popup! :products.selector/view]}
                                :label            (if multi-select? :select-products! :select-product!)}]))

(defn- header
  []
  [:<> [label-bar]
       (if-let [first-data-received? @(r/subscribe [:item-lister/first-data-received? :products.selector])]
               [control-bar]
               [elements/horizontal-separator {:size :xxl}])])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn view
  [popup-id]
  [popup-a/layout popup-id
                  {:footer              #'footer
                   :body                #'body
                   :header              #'header
                   :min-width           :m
                   :stretch-orientation :vertical}])
