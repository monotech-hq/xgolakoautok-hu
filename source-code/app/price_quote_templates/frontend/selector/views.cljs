
(ns app.price-quote-templates.frontend.selector.views
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
  (let [selected-template-count @(r/subscribe [:item-lister/get-selected-item-count :price-quote-templates.selector])
        on-discard-selection     [:item-lister/discard-selection! :price-quote-templates.selector]]
       [common/item-selector-footer :price-quote-templates.selector
                                    {:on-discard-selection on-discard-selection
                                     :selected-item-count  selected-template-count}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- template-list-item
  ; @param (keyword) selector-id
  ; @param (map) selector-props
  ; @param (integer) item-dex
  ; @param (map) template-item
  [selector-id _ item-dex {:keys [id issuer-logo modified-at name]}]
  (let [timestamp @(r/subscribe [:x.activities/get-actual-timestamp modified-at])]
       [components/item-list-row {:cells [[components/list-item-gap       {:width 12}]
                                          [components/list-item-thumbnail {:thumbnail (:media/uri issuer-logo)}]
                                          [components/list-item-gap       {:width 12}]
                                          [components/list-item-cell      {:rows [{:content name :placeholder :unnamed-price-quote-template}
                                                                                  {:content timestamp :font-size :xs :color :muted}]}]
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
       [common/item-selector-body :price-quote-templates.selector
                                  {:items-path        [:price-quote-templates :selector/downloaded-items]
                                   :list-item-element #'template-list-item}]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- control-bar
  []
  (let [selector-disabled? @(r/subscribe [:item-lister/lister-disabled? :price-quote-templates.selector])]
       [common/item-selector-control-bar :price-quote-templates.selector
                                         {:disabled?                selector-disabled?
                                          :search-field-placeholder :search-in-price-quote-templates}]))

(defn- label-bar
  []
  (let [multi-select? @(r/subscribe [:item-lister/get-meta-item :price-quote-templates.selector :multi-select?])]
       [common/item-selector-label-bar :price-quote-templates.selector
                                       {:label    (if multi-select? :select-price-quote-templates! :select-price-quote-template!)
                                        :on-close [:x.ui/remove-popup! :price-quote-templates.selector/view]}]))

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
