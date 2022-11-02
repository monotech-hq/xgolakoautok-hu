
(ns app.common.frontend.selector-item-marker.views
    (:require [app.common.frontend.list-item-marker.views :as list-item-marker.views]
              [elements.api                               :as elements]
              [re-frame.api                               :as r]))

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
                  [list-item-marker.views/element selector-id item-dex {:icon :check_circle_outline :progress 100 :progress-duration 1000}]
                  [list-item-marker.views/element selector-id item-dex {:icon :check_circle_outline}])
          [list-item-marker.views/element selector-id item-dex {:icon :radio_button_unchecked}]))

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
