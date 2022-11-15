
(ns app.components.frontend.list-item-drag-handle.views
    (:require [elements.api      :as elements]
              [mid-fruits.random :as random]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- list-item-drag-handle-body
  ; @param (keyword) handle-id
  ; @param (map) handle-props
  ;  {:drag-attributes (map)
  ;   :indent (map)(opt)}
  [_ {:keys [drag-attributes indent]}]
  [:div (merge drag-attributes {:style {:align-items "center"
                                        :cursor      "grab"
                                        :display     "flex"
                                        :height      "48px"}})
        [elements/icon {:icon :drag_handle}]])

(defn- list-item-drag-handle
  ; @param (keyword) cell-id
  ; @param (map) cell-props
  ;  {:class (keyword or keywords in vector)(opt)
  ;   :disabled? (boolean)(opt)
  ;   :indent (map)(opt)
  ;   :style (map)(opt)}
  [handle-id {:keys [class disabled? indent style] :as handle-props}]
  [elements/blank handle-id
                  {:class     class
                   :disabled? disabled?
                   :indent    indent
                   :content   [list-item-drag-handle-body handle-id handle-props ]
                   :style     style}])

(defn component
  ; @param (keyword)(opt) handle-id
  ; @param (map)(opt) handle-props
  ;  {:class (keyword or keywords in vector)(opt)
  ;   :disabled? (boolean)(opt)
  ;    Default: false
  ;   :drag-attributes (map)
  ;   :indent (map)(opt)
  ;   :style (map)(opt)}
  ;
  ; @usage
  ;  [list-item-drag-handle {...}]
  ;
  ; @usage
  ;  [list-item-drag-handle :my-handle {...}]
  ([handle-props]
   [component (random/generate-keyword) handle-props])

  ([handle-id handle-props]
   [list-item-drag-handle handle-id handle-props]))
