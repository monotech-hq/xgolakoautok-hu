
(ns app.clients.frontend.selector.views
    (:require [app.common.frontend.api     :as common]
              [app.components.frontend.api :as components]
              [elements.api                :as elements]
              [engines.item-lister.api     :as item-lister]
              [layouts.popup-a.api         :as popup-a]
              [re-frame.api                :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- footer
  []
  (let [selected-client-count @(r/subscribe [:item-lister/get-selected-item-count :clients.selector])
        on-discard-selection [:item-lister/discard-selection! :clients.selector]]
       [common/item-selector-footer :clients.selector
                                    {:on-discard-selection on-discard-selection
                                     :selected-item-count  selected-client-count}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- client-item-structure
  [selector-id item-dex {:keys [colors company-name email-address id modified-at] :as client-item}]
  (let [timestamp   @(r/subscribe [:x.activities/get-actual-timestamp modified-at])
        item-last?  @(r/subscribe [:item-lister/item-last? selector-id item-dex])
        client-name @(r/subscribe [:clients.selector/get-client-name item-dex])]
       [common/list-item-structure {:cells [[elements/color-marker {:colors colors :indent {:left :xs :right :m :horizontal :xs} :size :m}]
                                            [components/list-item-cell {:rows [{:content client-name   :placeholder :unnamed-client}
                                                                               {:content email-address :font-size :xs :color :muted}
                                                                               {:content company-name  :font-size :xs :color :muted}]
                                                                        :width :stretch}]
                                            [common/selector-item-marker selector-id item-dex {:item-id id}]]
                                    :separator (if-not item-last? :bottom)}]))

(defn- client-item
  [selector-id item-dex {:keys [id] :as client-item}]
  [elements/toggle {:content     [client-item-structure selector-id item-dex client-item]
                    :hover-color :highlight
                    :on-click    [:item-selector/item-clicked :clients.selector id]}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- client-list
  []
  (let [items @(r/subscribe [:item-lister/get-downloaded-items :clients.selector])]
       [common/item-list :clients.selector {:item-element #'client-item :items items}]))

(defn- client-lister
  []
  [item-lister/body :clients.selector
                    {:default-order-by :modified-at/descending
                     :items-path       [:clients :selector/downloaded-items]
                     :error-element    [components/error-content {:error :the-content-you-opened-may-be-broken}]
                     :ghost-element    [common/item-selector-ghost-element]
                     :list-element     [client-list]}])

(defn- body
  []
  [:<> [elements/horizontal-separator {:size :xs}]
       [client-lister]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- control-bar
  []
  (let [selector-disabled? @(r/subscribe [:item-lister/lister-disabled? :clients.selector])]
       [common/item-selector-control-bar :clients.selector
                                         {:disabled?        selector-disabled?
                                          :order-by-options [:modified-at/ascending :modified-at/descending :name/ascending :name/descending]
                                          :search-field-placeholder :search-in-clients
                                          :search-keys      [:name :email-address :phone-number]}]))

(defn- label-bar
  []
  (let [multi-select? @(r/subscribe [:item-lister/get-meta-item :clients.selector :multi-select?])]
       [components/popup-label-bar :clients.selector/view
                                   {:primary-button   {:label :save! :on-click [:item-selector/save-selection! :clients.selector]}
                                    :secondary-button (if-let [autosaving? @(r/subscribe [:item-selector/autosaving? :clients.selector])]
                                                              {:label :abort!  :on-click [:item-selector/abort-autosave! :clients.selector]}
                                                              {:label :cancel! :on-click [:x.ui/remove-popup! :clients.selector/view]})
                                    :label            (if multi-select? :select-clients! :select-client!)}]))

(defn- header
  []
  [:<> [label-bar]
       (if-let [first-data-received? @(r/subscribe [:item-lister/first-data-received? :clients.selector])]
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
