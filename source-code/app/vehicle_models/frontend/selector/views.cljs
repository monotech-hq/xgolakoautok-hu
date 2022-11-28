
(ns app.vehicle-models.frontend.selector.views
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
  (let [selected-model-count @(r/subscribe [:item-lister/get-selected-item-count :vehicle-models.selector])
        on-discard-selection [:item-lister/discard-selection! :vehicle-models.selector]]
       [common/item-selector-footer :vehicle-models.selector
                                    {:on-discard-selection on-discard-selection
                                     :selected-item-count  selected-model-count}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- model-list-item
  ; @param (keyword) selector-id
  ; @param (map) selector-props
  ;  {:countable? (boolean)(opt)}
  ; @param (integer) item-dex
  ; @param (map) model-item
  [selector-id {:keys [countable?]} item-dex {:keys [id modified-at name thumbnail]}]
  (let [timestamp   @(r/subscribe [:x.activities/get-actual-timestamp modified-at])
        model-count @(r/subscribe [:item-selector/get-item-count selector-id id])
        model-count  {:content :n-pieces :replacements [model-count]}]
       [components/item-list-row {:cells [[components/list-item-gap       {:width 12}]
                                          (if countable? [common/selector-item-counter selector-id item-dex {:item-id id}])
                                          (if countable? [components/list-item-gap {:width 12}])
                                          [components/list-item-thumbnail {:thumbnail (:media/uri thumbnail)}]
                                          [components/list-item-gap       {:width 12}]
                                          [components/list-item-cell      {:rows [{:content name :placeholder :unnamed-vehicle-model}
                                                                                  {:content timestamp :font-size :xs :color :muted}
                                                                                  (if countable? {:content model-count :font-size :xs :color :muted})]}]
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
       [common/item-selector-body :vehicle-models.selector
                                  {:items-path        [:vehicle-models :selector/downloaded-items]
                                   :list-item-element #'model-list-item}]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- control-bar
  []
  (let [selector-disabled? @(r/subscribe [:item-lister/lister-disabled? :vehicle-models.selector])]
       [common/item-selector-control-bar :vehicle-models.selector
                                         {:disabled?                selector-disabled?
                                          :search-field-placeholder :search-in-vehicle-models}]))

(defn- label-bar
  []
  (let [multi-select? @(r/subscribe [:item-lister/get-meta-item :vehicle-models.selector :multi-select?])]
       [common/item-selector-label-bar :vehicle-models.selector
                                       {:label    (if multi-select? :select-vehicle-models! :select-vehicle-model!)
                                        :on-close [:x.ui/remove-popup! :vehicle-models.selector/view]}]))

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
