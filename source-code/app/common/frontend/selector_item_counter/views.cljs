
(ns app.common.frontend.selector-item-counter.views
    (:require [elements.api :as elements]
              [re-frame.api :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- selector-item-increase-count-button
  ; @param (keyword) selector-id
  ; @param (integer) item-dex
  ; @param (map) counter-props
  ;  {:item-id (string)}
  [selector-id _ {:keys [item-id]}]
  (let [item-selected? @(r/subscribe [:item-lister/item-selected?   selector-id item-id])
        item-count     @(r/subscribe [:item-selector/get-item-count selector-id item-id])
        disabled?       (or (not item-selected?) (= item-count 255))]
       [elements/icon-button {:icon :add :height :s :stop-propagation? true
                              :on-click [:item-selector/increase-item-count! selector-id item-id]
                              :disabled? disabled? :color (if disabled? :muted :default)}]))

(defn- selector-item-decrease-count-button
  ; @param (keyword) selector-id
  ; @param (integer) item-dex
  ; @param (map) counter-props
  ;  {:item-id (string)}
  [selector-id _ {:keys [item-id]}]
  (let [item-selected? @(r/subscribe [:item-lister/item-selected?   selector-id item-id])
        item-count     @(r/subscribe [:item-selector/get-item-count selector-id item-id])
        disabled?       (or (not item-selected?) (= item-count 1))]
       [elements/icon-button {:icon :remove :height :s :stop-propagation? true
                              :on-click [:item-selector/decrease-item-count! selector-id item-id]
                              :disabled? disabled? :color (if disabled? :muted :default)}]))

(defn- selector-item-counter
  ; @param (keyword) selector-id
  ; @param (integer) item-dex
  ; @param (map) counter-props
  [selector-id item-dex counter-props]
  [:div {:style {:padding "0 0px 0 12px"}}
        [selector-item-increase-count-button selector-id item-dex counter-props]
        [selector-item-decrease-count-button selector-id item-dex counter-props]])

(defn element
  ; @param (keyword) selector-id
  ; @param (integer) item-dex
  ; @param (map) counter-props
  ;
  ; @usage
  ;  [selector-item-counter :my-selector 42 {...}]
  [selector-id item-dex counter-props]
  [selector-item-counter selector-id item-dex counter-props])
