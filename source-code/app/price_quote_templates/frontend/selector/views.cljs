
(ns app.price-quote-templates.frontend.selector.views
    (:require [app.common.frontend.api :as common]
              [elements.api            :as elements]
              [engines.item-lister.api :as item-lister]
              [layouts.popup-a.api     :as popup-a]
              [re-frame.api            :as r]
              [x.components.api        :as components]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- footer
  []
  (let [selected-template-count @(r/subscribe [:item-lister/get-selected-item-count :price-quote-templates.selector])
        on-discard-selection     [:item-lister/discard-selection! :price-quote-templates.selector]]
       [common/item-selector-footer :price-quote-templates.selector
                                    {:on-discard-selection on-discard-selection
                                     :selected-item-count  selected-template-count}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- template-item-structure
  [selector-id item-dex {:keys [id issuer-logo modified-at name] :as template-item}]
  (let [timestamp  @(r/subscribe [:x.activities/get-actual-timestamp modified-at])
        item-last? @(r/subscribe [:item-lister/item-last? selector-id item-dex])]
       [common/list-item-structure {:cells [[common/list-item-thumbnail    {:thumbnail (:media/uri issuer-logo)}]
                                            [common/list-item-primary-cell {:label name :timestamp timestamp :stretch? true :placeholder :unnamed-price-quote-template}]
                                            [common/selector-item-marker   selector-id item-dex {:item-id id}]]
                                    :separator (if-not item-last? :bottom)}]))

(defn- template-item
  [selector-id item-dex {:keys [id] :as template-item}]
  [elements/toggle {:content     [template-item-structure selector-id item-dex template-item]
                    :hover-color :highlight
                    :on-click    [:item-selector/item-clicked :price-quote-templates.selector id]}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- template-list
  [lister-id items]
  [common/item-list lister-id {:item-element #'template-item :items items}])

(defn- template-lister
  []
  [item-lister/body :price-quote-templates.selector
                    {:default-order-by :modified-at/descending
                     :items-path       [:price-quote-templates :selector/downloaded-items]
                     :error-element    [common/error-content {:error :the-content-you-opened-may-be-broken}]
                     :ghost-element    #'common/item-selector-ghost-element
                     :list-element     #'template-list}])

(defn- body
  []
  [:<> [elements/horizontal-separator {:size :xs}]
       [template-lister]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- control-bar
  []
  (let [selector-disabled? @(r/subscribe [:item-lister/lister-disabled? :price-quote-templates.selector])]
       [common/item-selector-control-bar :price-quote-templates.selector
                                         {:disabled?        selector-disabled?
                                          :order-by-options [:modified-at/ascending :modified-at/descending :name/ascending :name/descending]
                                          :search-field-placeholder :search-in-price-quote-templates
                                          :search-keys      [:name]}]))

(defn- label-bar
  []
  [common/popup-label-bar :price-quote-templates.selector/view
                          {:primary-button   {:label :save! :on-click [:item-selector/save-selection! :price-quote-templates.selector]}
                           :secondary-button (if-let [autosaving? @(r/subscribe [:item-selector/autosaving? :price-quote-templates.selector])]
                                                     {:label :abort!  :on-click [:item-selector/abort-autosave! :price-quote-templates.selector]}
                                                     {:label :cancel! :on-click [:x.ui/remove-popup! :price-quote-templates.selector/view]})
                           :label            :select-price-quote-template!}])

(defn- header
  []
  [:<> [label-bar]
       (if-let [first-data-received? @(r/subscribe [:item-lister/first-data-received? :price-quote-templates.selector])]
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
