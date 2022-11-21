
(ns app.vehicle-models.frontend.selector.views
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
  (let [selected-model-count @(r/subscribe [:item-lister/get-selected-item-count :vehicle-models.selector])
        on-discard-selection [:item-lister/discard-selection! :vehicle-models.selector]]
       [common/item-selector-footer :vehicle-models.selector
                                    {:on-discard-selection on-discard-selection
                                     :selected-item-count  selected-model-count}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- model-item-structure
  [selector-id item-dex {:keys [id modified-at name thumbnail]}]
  (let [timestamp @(r/subscribe [:x.activities/get-actual-timestamp modified-at])
        item-last? @(r/subscribe [:item-lister/item-last? selector-id item-dex])]
       [common/list-item-structure {:cells [[    {:thumbnail (:media/uri thumbnail)}]
                                            [common/list-item-primary-cell {:label name :timestamp timestamp :stretch? true :placeholder :unnamed-vehicle-model}]
                                            [common/selector-item-marker   selector-id item-dex {:item-id id}]]
                                    :separator (if-not item-last? :bottom)}]))

(defn- model-item
  [selector-id item-dex {:keys [id] :as model-item}]
  [elements/toggle {:content     [model-item-structure selector-id item-dex model-item]
                    :hover-color :highlight
                    :on-click    [:item-selector/item-clicked :vehicle-models.selector id]}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- model-list
  []
  (let [items @(r/subscribe [:item-lister/get-downloaded-items :vehicle-models.selector])]
       [common/item-list :vehicle-models.selector {:item-element #'model-item :items items}]))

(defn- model-lister
  []
  [item-lister/body :vehicle-models.selector
                    {:default-order-by :modified-at/descending
                     :items-path       [:vehicle-models :selector/downloaded-items]
                     :error-element    [components/error-content {:error :the-content-you-opened-may-be-broken}]
                     :ghost-element    [common/item-selector-ghost-element]
                     :list-element     [model-list]}])

(defn- body
  []
  [:<> [elements/horizontal-separator {:size :xs}]
       [model-lister]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- control-bar
  []
  (let [selector-disabled? @(r/subscribe [:item-lister/lister-disabled? :vehicle-models.selector])]
       [common/item-selector-control-bar :vehicle-models.selector
                                         {:disabled?        selector-disabled?
                                          :order-by-options [:modified-at/ascending :modified-at/descending :name/ascending :name/descending]
                                          :search-field-placeholder :search-in-vehicle-models
                                          :search-keys      [:name]}]))

(defn- label-bar
  []
  (let [multi-select? @(r/subscribe [:item-lister/get-meta-item :vehicle-models.selector :multi-select?])]
       [components/popup-label-bar :vehicle-models.selector/view
                                    {:primary-button   {:label :save! :on-click [:item-selector/save-selection! :vehicle-models.selector]}
                                     :secondary-button (if-let [autosaving? @(r/subscribe [:item-selector/autosaving? :vehicle-models.selector])]
                                                               {:label :abort!  :on-click [:item-selector/abort-autosave! :vehicle-models.selector]}
                                                               {:label :cancel! :on-click [:x.ui/remove-popup! :vehicle-models.selector/view]})
                                     :label            (if multi-select? :select-vehicle-models! :select-vehicle-model!)}]))

(defn- header
  []
  [:<> [label-bar]
       (if-let [first-data-received? @(r/subscribe [:item-lister/first-data-received? :vehicle-models.selector])]
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
