
(ns app.vehicle-types.frontend.selector.views
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
  (let [selected-type-count @(r/subscribe [:item-lister/get-selected-item-count :vehicle-types.selector])
        on-discard-selection [:item-lister/discard-selection! :vehicle-types.selector]]
       [common/item-selector-footer :vehicle-types.selector
                                    {:on-discard-selection on-discard-selection
                                     :selected-item-count  selected-type-count}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- type-list-item
  ; @param (keyword) selector-id
  ; @param (map) selector-props
  ;  {:countable? (boolean)(opt)}
  ; @param (integer) item-dex
  ; @param (map) type-item
  [selector-id {:keys [countable?]} item-dex {:keys [id modified-at name]}]
  (let [timestamp  @(r/subscribe [:x.activities/get-actual-timestamp modified-at])
        type-count @(r/subscribe [:item-selector/get-item-count selector-id id])
        type-count  {:content :n-pieces :replacements [type-count]}]
       [components/item-list-row {:cells [(if countable? [components/list-item-gap {:width 12}])
                                          (if countable? [common/selector-item-counter selector-id item-dex {:item-id id}])
                                          [components/list-item-thumbnail {:icon :text_snippet :icon-family :material-icons-outlined}]
                                          [components/list-item-cell {:rows [{:content name :placeholder :unnamed-vehicle-type}
                                                                             {:content timestamp :font-size :xs :color :muted}
                                                                             (if countable? {:content type-count :font-size :xs :color :muted})]}]
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
       [common/item-selector-body :vehicle-types.selector
                                  {:items-path        [:vehicle-types :selector/downloaded-items]
                                   :list-item-element #'type-list-item}]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- control-bar
  []
  (let [selector-disabled? @(r/subscribe [:item-lister/lister-disabled? :vehicle-types.selector])]
       [common/item-selector-control-bar :vehicle-types.selector
                                         {:disabled?                selector-disabled?
                                          :search-field-placeholder :search-in-vehicle-types}]))

(defn- label-bar
  []
  (let [multi-select? @(r/subscribe [:item-lister/get-meta-item :vehicle-types.selector :multi-select?])]
       [common/item-selector-label-bar :vehicle-types.selector
                                       {:label    (if multi-select? :select-vehicle-types! :select-vehicle-type!)
                                        :on-close [:x.ui/remove-popup! :vehicle-types.selector/view]}]))

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
