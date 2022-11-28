
(ns app.website-menus.frontend.selector.views
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
  (let [selected-client-count @(r/subscribe [:item-lister/get-selected-item-count :website-menus.selector])
        on-discard-selection [:item-lister/discard-selection! :website-menus.selector]]
       [common/item-selector-footer :website-menus.selector
                                    {:on-discard-selection on-discard-selection
                                     :selected-item-count  selected-client-count}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- menu-list-item
  ; @param (keyword) selector-id
  ; @param (map) selector-props
  ; @param (integer) item-dex
  ; @param (map) menu-item
  [selector-id _ item-dex {:keys [id name]}]
  [components/item-list-row {:cells []
                             :border (if (not= item-dex 0) :top)}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- menu-lister
  []
  [item-lister/body :website-menus.selector
                    {:default-order-by :modified-at/descending
                     :items-path       [:website-menus :selector/downloaded-items]
                     :error-element    [components/error-content {:error :the-content-you-opened-may-be-broken}]
                     :ghost-element    [components/ghost-view    {:layout :item-list :item-count 3}]
                     :list-element     [common/item-selector-body :website-menus.selector {:list-item-element #'menu-list-item}]}])

(defn- body
  []
  [:<> [elements/horizontal-separator {:height :xs}]
       [menu-lister]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- control-bar
  []
  (let [selector-disabled? @(r/subscribe [:item-lister/lister-disabled? :website-menus.selector])]
       [common/item-selector-control-bar :website-menus.selector
                                         {:disabled?        selector-disabled?
                                          :order-by-options [:modified-at/ascending :modified-at/descending :name/ascending :name/descending]
                                          :search-field-placeholder :search-in-website-menus
                                          :search-keys      [:name]}]))

(defn- label-bar
  []
  (let [multi-select? @(r/subscribe [:item-lister/get-meta-item :website-menus.selector :multi-select?])]
       [components/popup-label-bar ::label-bar
                                   {:primary-button   {:label :save! :on-click [:item-selector/save-selection! :website-menus.selector]}
                                    :secondary-button (if-let [autosaving? @(r/subscribe [:item-selector/autosaving? :website-menus.selector])]
                                                              {:label :abort!  :on-click [:item-selector/abort-autosave! :website-menus.selector]}
                                                              {:label :cancel! :on-click [:x.ui/remove-popup! :website-menus.selector/view]})
                                    :label            (if multi-select? :select-website-menus! :select-website-menu!)}]))

(defn- header
  []
  [:<> [label-bar]
       (if-let [first-data-received? @(r/subscribe [:item-lister/first-data-received? :website-menus.selector])]
               [control-bar]
               [elements/horizontal-separator {:height :xxl}])])

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
