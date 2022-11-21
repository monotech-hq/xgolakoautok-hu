
(ns app.components.frontend.list-item-button.views
    (:require [app.components.frontend.list-item-button.prototypes :as list-item-button.prototypes]
              [elements.api                                        :as elements]
              [random.api                                          :as random]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- list-item-button
  ; @param (keyword) button-id
  ; @param (map) button-props
  [button-id button-props]
  [elements/button button-id button-props])

(defn component
  ; @param (keyword)(opt) button-id
  ; @param (map) button-props
  ;  {:class (keyword or keywords in vector)(opt)
  ;   :disabled? (boolean)(opt)
  ;    Default: false
  ;   :label (metamorphic-content)
  ;   :on-click (metamorphic-event)
  ;   :indent (map)(opt)
  ;   :style (map)(opt)}}
  ;
  ; @usage
  ;  [list-item-button {...}]
  ;
  ; @usage
  ;  [list-item-button :my-button {...}]
  ([button-props]
   [component (random/generate-keyword) button-props])

  ([button-id button-props]
   (let [button-props (list-item-button.prototypes/button-props-prototype button-props)]
        [list-item-button button-id button-props])))
