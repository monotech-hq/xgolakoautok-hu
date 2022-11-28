
(ns app.components.frontend.list-item-button.views
    (:require [app.components.frontend.list-item-button.prototypes :as list-item-button.prototypes]
              [css.api                                             :as css]
              [elements.api                                        :as elements]
              [random.api                                          :as random]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- list-item-button
  ; @param (keyword) button-id
  ; @param (map) button-props
  ;  {:width (px)}
  [button-id {:keys [width] :as button-props}]
  [:td {:style {:vertical-align "middle" :width (css/px width)}}
       [elements/button button-id button-props]])

(defn component
  ; @param (keyword)(opt) button-id
  ; @param (map) button-props
  ;  {:background-color (keyword)(opt)
  ;    Default: :highlight
  ;   :hover-color (keyword)(opt)
  ;    Default: :highlight
  ;   :icon (keyword)(opt)
  ;   :icon-family (keyword)(opt)
  ;    Default: :material-icons-filled
  ;    W/ {:icon ...}
  ;   :icon-position (keyword)(opt)
  ;    :left, :right
  ;    Default: :left
  ;    W/ {:icon ...}
  ;   :label (metamorphic-content)
  ;   :on-click (metamorphic-event)
  ;   :width (px)(opt)
  ;    Default: 120}
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
