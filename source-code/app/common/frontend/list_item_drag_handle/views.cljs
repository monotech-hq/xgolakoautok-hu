
(ns app.common.frontend.list-item-drag-handle.views
    (:require [elements.api :as elements]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- list-item-drag-handle
  ; @param (keyword) lister-id
  ; @param (integer) item-dex
  ; @param (map) dnd-kit-props
  ;  {:attributes (map)
  ;   :listeners (map)}
  [_ _ {:keys [attributes listeners] :as dnd-kit-props}]
  [:div (merge attributes listeners {:style {:align-items "center"
                                             :cursor      "grab"
                                             :display     "flex"
                                             :height      "48px"
                                             :margin-left "12px"}})
        [elements/icon {:icon :drag_indicator}]])

(defn element
  ; @param (keyword) lister-id
  ; @param (integer) item-dex
  ; @param (map) dnd-kit-props
  ;  {:attributes (map)
  ;   :listeners (map)}
  ;
  ; @usage
  ;  [list-item-drag-handle :my-lister 0 {...}]
  [lister-id item-dex dnd-kit-props]
  [list-item-drag-handle lister-id item-dex dnd-kit-props])
