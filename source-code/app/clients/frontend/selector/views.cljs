
(ns app.clients.frontend.selector.views
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
  (let [selected-client-count @(r/subscribe [:item-lister/get-selected-item-count :clients.selector])
        on-discard-selection [:item-lister/discard-selection! :clients.selector]]
       [common/item-selector-footer :clients.selector
                                    {:on-discard-selection on-discard-selection
                                     :selected-item-count  selected-client-count}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- client-list-item
  ; @param (keyword) selector-id
  ; @param (map) selector-props
  ; @param (integer) item-dex
  ; @param (map) client-item
  [selector-id _ item-dex {:keys [colors company-name first-name email-address id last-name]}]
  (let [client-name @(r/subscribe [:clients.selector/get-client-name item-dex])]
       [components/item-list-row {:cells [[components/list-item-avatar {:colors colors :first-name first-name :last-name last-name :size 42}]
                                          [components/list-item-cell   {:rows [{:content client-name :placeholder :unnamed-package}
                                                                               (if email-address {:content email-address :font-size :xs :color :muted})
                                                                               (if company-name  {:content company-name  :font-size :xs :color :muted})]}]
                                          [components/list-item-gap    {:width 6}]
                                          [common/selector-item-marker selector-id item-dex {:item-id id}]
                                          [components/list-item-gap    {:width 6}]]
                                  :border (if (not= item-dex 0) :top)}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- body
  ; @param (keyword) popup-id
  [_]
  [:<> [elements/horizontal-separator {:height :xs}]
       [common/item-selector-body :clients.selector
                                  {:items-path        [:clients :selector/downloaded-items]
                                   :list-item-element #'client-list-item}]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- control-bar
  []
  (let [selector-disabled? @(r/subscribe [:item-lister/lister-disabled? :clients.selector])]
       [common/item-selector-control-bar :clients.selector
                                         {:disabled?                selector-disabled?
                                          :search-field-placeholder :search-in-clients
                                          :search-keys              [:name :email-address :phone-number]}]))

(defn- label-bar
  []
  (let [multi-select? @(r/subscribe [:item-lister/get-meta-item :clients.selector :multi-select?])]
       [common/item-selector-label-bar :clients.selector
                                       {:label    (if multi-select? :select-clients! :select-client!)
                                        :on-close [:x.ui/remove-popup! :clients.selector/view]}]))

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
