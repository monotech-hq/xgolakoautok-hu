
(ns app.common.frontend.selector-item-marker.views
    (:require [app.components.frontend.api :as components]
              [elements.api                :as elements]
              [re-frame.api                :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- selector-item-marker
  ; @param (keyword) selector-id
  ; @param (integer) item-dex
  ; @param (map) marker-props
  ;  {:item-id (string)}
  [selector-id item-dex {:keys [item-id]}]
  (if-let [item-selected? @(r/subscribe [:item-lister/item-selected? selector-id item-id])]
          (if-let [autosaving? @(r/subscribe [:item-selector/autosaving? selector-id])]
                  [components/list-item-marker {:icon :check_circle_outline :progress 100 :progress-duration 1000}]
                  [components/list-item-marker {:icon :check_circle_outline}])
          [components/list-item-marker {:icon :radio_button_unchecked}]))

(defn element
  ; @param (keyword) selector-id
  ; @param (integer) item-dex
  ; @param (map) marker-props
  ;  {:item-id (string)}
  ;
  ; @usage
  ;  [selector-item-marker :my-selector 42 {...}]
  [selector-id item-dex marker-props]
  [selector-item-marker selector-id item-dex marker-props])
