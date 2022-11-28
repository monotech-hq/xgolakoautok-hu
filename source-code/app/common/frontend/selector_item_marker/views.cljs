
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
  ;  {:disabled? (boolean)(opt)
  ;   :item-id (string)}
  [selector-id item-dex {:keys [disabled? item-id]}]
  ; BUG#0781 (source-code/app/components/frontend/item_list_table/views.cljs)
  [:td {:style {:width "72px"}}
       (let [item-selected? @(r/subscribe [:item-selector/item-selected? selector-id item-id])
             autosaving?    @(r/subscribe [:item-selector/autosaving?    selector-id item-id])]
            [elements/icon-button {:disabled? disabled?
                                   :icon (if item-selected? :check_circle_outline :radio_button_unchecked)
                                   :on-click [:item-selector/item-clicked selector-id item-id]
                                   :progress-duration 1000
                                   :progress (if (and item-selected? autosaving?) 100 0)
                                   :hover-color :highlight}])])

(defn element
  ; @param (keyword) selector-id
  ; @param (integer) item-dex
  ; @param (map) marker-props
  ;  {:disabled? (boolean)(opt)
  ;    Default: false
  ;   :item-id (string)}
  ;
  ; @usage
  ;  [selector-item-marker :my-selector 42 {...}]
  [selector-id item-dex marker-props]
  [selector-item-marker selector-id item-dex marker-props])
