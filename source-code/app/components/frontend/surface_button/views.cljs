
(ns app.components.frontend.surface-button.views
    (:require [app.components.frontend.surface-button.prototypes :as surface-button.prototypes]
              [elements.api                                      :as elements]
              [mid-fruits.random                                 :as random]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn component
  ; @param (keyword)(opt) button-id
  ; @param (map) button-props
  ;  {}
  ;
  ; @usage
  ;  [surface-button {...}]
  ;
  ; @usage
  ;  [surface-button :my-surface-button {...}]
  ([button-props]
   [component (random/generate-keyword) button-props])

  ([button-id button-props]
   (let [button-props (surface-button.prototypes/button-props-prototype button-props)]
        [elements/button button-id button-props])))
