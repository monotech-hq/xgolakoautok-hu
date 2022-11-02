
(ns app.models.frontend.selector.views
    (:require [app.common.frontend.api :as common]
              [elements.api            :as elements]
              [engines.item-lister.api :as item-lister]
              [layouts.popup-a.api     :as popup-a]
              [re-frame.api            :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- footer
  []
  (let [selected-model-count @(r/subscribe [:item-lister/get-selected-item-count :models.selector])
        on-discard-selection [:item-lister/discard-selection! :models.selector]]
       [common/item-selector-footer :models.selector
                                    {:on-discard-selection on-discard-selection
                                     :selected-item-count  selected-model-count}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- model-item-structure
  [selector-id item-dex {:keys [id modified-at name thumbnail]}]
  (let [timestamp @(r/subscribe [:activities/get-actual-timestamp modified-at])]
       [common/list-item-structure selector-id item-dex
                                   {:cells [[common/list-item-thumbnail    selector-id item-dex {:thumbnail (:media/uri thumbnail)}]
                                            [common/list-item-primary-cell selector-id item-dex {:label name :timestamp timestamp :stretch? true
                                                                                                 :placeholder :unnamed-model}]
                                            [common/selector-item-marker   selector-id item-dex {:item-id id}]]}]))

(defn- model-item
  [selector-id item-dex {:keys [id] :as model-item}]
  [elements/toggle {:content     [model-item-structure selector-id item-dex model-item]
                    :hover-color :highlight
                    :on-click    [:item-selector/item-clicked :models.selector id]}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- model-list
  [lister-id items]
  [common/item-list lister-id {:item-element #'model-item :items items}])

(defn- model-lister
  []
  [item-lister/body :models.selector
                    {:default-order-by :modified-at/descending
                     :items-path       [:models :selector/downloaded-items]
                     :error-element    [common/error-content {:error :the-content-you-opened-may-be-broken}]
                     :ghost-element    #'common/item-selector-ghost-element
                     :list-element     #'model-list}])

(defn- body
  []
  [:<> [elements/horizontal-separator {:size :xs}]
       [model-lister]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- control-bar
  []
  (let [selector-disabled? @(r/subscribe [:item-lister/lister-disabled? :models.selector])]
       [common/item-selector-control-bar :models.selector
                                         {:disabled?        selector-disabled?
                                          :order-by-options [:modified-at/ascending :modified-at/descending :name/ascending :name/descending]
                                          :search-field-placeholder :search-in-models
                                          :search-keys      [:name]}]))

(defn- label-bar
  []
  (let [multi-select? @(r/subscribe [:item-lister/get-meta-item :models.selector :multi-select?])]
       [common/popup-label-bar :models.selector/view
                               {:primary-button   {:label :save! :on-click [:item-selector/save-selection! :models.selector]}
                                :secondary-button (if-let [autosaving? @(r/subscribe [:item-selector/autosaving? :models.selector])]
                                                          {:label :abort!  :on-click [:item-selector/abort-autosave! :models.selector]}
                                                          {:label :cancel! :on-click [:ui/remove-popup! :models.selector/view]})
                                :label            (if multi-select? :select-models! :select-model!)}]))

(defn- header
  []
  [:<> [label-bar]
       (if-let [first-data-received? @(r/subscribe [:item-lister/first-data-received? :models.selector])]
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
