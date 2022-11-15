
(ns app.services.frontend.selector.views
    (:require [app.common.frontend.api :as common]
              [elements.api            :as elements]
              [engines.item-lister.api :as item-lister]
              [layouts.popup-a.api     :as popup-a]
              [re-frame.api            :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- footer
  []
  (let [selected-service-count @(r/subscribe [:item-lister/get-selected-item-count :services.selector])
        on-discard-selection [:item-lister/discard-selection! :services.selector]]
       [common/item-selector-footer :services.selector
                                    {:on-discard-selection on-discard-selection
                                     :selected-item-count  selected-service-count}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- service-item-structure
  [selector-id item-dex {:keys [id modified-at name quantity-unit]}]
  (let [timestamp     @(r/subscribe [:x.activities/get-actual-timestamp modified-at])
        item-last?    @(r/subscribe [:item-lister/item-last? selector-id item-dex])
        service-count @(r/subscribe [:item-selector/get-item-count selector-id id])
        item-count     {:content (:value quantity-unit) :replacements [service-count]}]
       [common/list-item-structure {:cells [[common/selector-item-counter  selector-id item-dex {:item-id id}]
                                            [common/list-item-thumbnail    {:icon :workspace_premium}]
                                            [common/list-item-primary-cell {:label name :timestamp timestamp :stretch? true :placeholder :unnamed-service :description item-count}]
                                            [common/selector-item-marker   selector-id item-dex {:item-id id}]]
                                    :separator (if-not item-last? :bottom)}]))

(defn- service-item
  [selector-id item-dex {:keys [id] :as service-item}]
  [elements/toggle {:content     [service-item-structure selector-id item-dex service-item]
                    :hover-color :highlight
                    :on-click    [:item-selector/item-clicked :services.selector id]}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- service-list
  [lister-id items]
  [common/item-list lister-id {:item-element #'service-item :items items}])

(defn- service-lister
  []
  [item-lister/body :services.selector
                    {:default-order-by :modified-at/descending
                     :items-path       [:services :selector/downloaded-items]
                     :error-element    [common/error-content {:error :the-content-you-opened-may-be-broken}]
                     :ghost-element    #'common/item-selector-ghost-element
                     :list-element     #'service-list}])

(defn- body
  []
  [:<> [elements/horizontal-separator {:size :xs}]
       [service-lister]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- control-bar
  []
  (let [selector-disabled? @(r/subscribe [:item-lister/lister-disabled? :services.selector])]
       [common/item-selector-control-bar :services.selector
                                         {:disabled?        selector-disabled?
                                          :order-by-options [:modified-at/ascending :modified-at/descending :name/ascending :name/descending]
                                          :search-field-placeholder :search-in-services
                                          :search-keys      [:item-number :name]}]))

(defn- label-bar
  []
  (let [multi-select? @(r/subscribe [:item-lister/get-meta-item :services.selector :multi-select?])]
       [common/popup-label-bar :services.selector/view
                               {:primary-button   {:label :save!   :on-click [:item-selector/save-selection! :services.selector]}
                                :secondary-button {:label :cancel! :on-click [:x.ui/remove-popup! :services.selector/view]}
                                :label            (if multi-select? :select-services! :select-service!)}]))

(defn- header
  []
  [:<> [label-bar]
       (if-let [first-data-received? @(r/subscribe [:item-lister/first-data-received? :services.selector])]
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
