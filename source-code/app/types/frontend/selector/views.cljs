
(ns app.types.frontend.selector.views
    (:require [app.common.frontend.api :as common]
              [elements.api            :as elements]
              [engines.item-lister.api :as item-lister]
              [layouts.popup-a.api     :as popup-a]
              [re-frame.api            :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- footer
  []
  (let [selected-type-count @(r/subscribe [:item-lister/get-selected-item-count :types.selector])
        on-discard-selection [:item-lister/discard-selection! :types.selector]]
       [common/item-selector-footer :types.selector
                                    {:on-discard-selection on-discard-selection
                                     :selected-item-count  selected-type-count}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- type-item-structure
  [selector-id item-dex {:keys [id modified-at name thumbnail]}]
  (let [timestamp @(r/subscribe [:activities/get-actual-timestamp modified-at])]
       [common/list-item-structure selector-id item-dex
                                   {:cells [[common/list-item-thumbnail    selector-id item-dex {:thumbnail (:media/uri thumbnail)}]
                                            [common/list-item-primary-cell selector-id item-dex {:label name :timestamp timestamp :stretch? true :placeholder :unnamed-type}]
                                            [common/selector-item-marker   selector-id item-dex {:item-id id}]]}]))

(defn- type-item
  [selector-id item-dex {:keys [id] :as type-item}]
  [elements/toggle {:content     [type-item-structure selector-id item-dex type-item]
                    :hover-color :highlight
                    :on-click    [:item-selector/item-clicked :types.selector id]}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- type-list
  [lister-id items]
  [common/item-list lister-id {:item-element #'type-item :items items}])

(defn- type-lister
  []
  (let [model-id @(r/subscribe [:item-lister/get-meta-item :types.selector :model-id])]
       [item-lister/body :types.selector
                         {:default-order-by :modified-at/descending
                          :error-element    [common/error-content {:error :the-content-you-opened-may-be-broken}]
                          :ghost-element    #'common/item-selector-ghost-element
                          :list-element     #'type-list
                          :items-path       [:types :selector/downloaded-items]
                          :prefilter        {:type/model-id model-id}}]))

(defn- body
  []
  [:<> [elements/horizontal-separator {:size :xs}]
       [type-lister]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- control-bar
  []
  (let [selector-disabled? @(r/subscribe [:item-lister/lister-disabled? :types.selector])]
       [common/item-selector-control-bar :types.selector
                                         {:disabled?        selector-disabled?
                                          :order-by-options [:modified-at/ascending :modified-at/descending :name/ascending :name/descending]
                                          :search-field-placeholder :search-in-types
                                          :search-keys      [:name]}]))

(defn- label-bar
  []
  (let [multi-select? @(r/subscribe [:item-lister/get-meta-item :types.selector :multi-select?])]
       [common/popup-label-bar :types.selector/view
                               {:primary-button   {:label :save!   :on-click [:item-selector/save-selection! :types.selector]}
                                :secondary-button (if-let [autosaving? @(r/subscribe [:item-selector/autosaving? :types.selector])]
                                                          {:label :abort!  :on-click [:item-selector/abort-autosave! :types.selector]}
                                                          {:label :cancel! :on-click [:ui/remove-popup! :types.selector/view]})
                                :label            (if multi-select? :select-types! :select-type!)}]))

(defn- header
  []
  [:<> [label-bar]
       (if-let [first-data-received? @(r/subscribe [:item-lister/first-data-received? :types.selector])]
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
