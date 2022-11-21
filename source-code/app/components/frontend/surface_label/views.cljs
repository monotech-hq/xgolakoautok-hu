
(ns app.components.frontend.surface-label.views
    (:require [elements.api          :as elements]
              [layouts.surface-a.api :as surface-a]
              [random.api            :as random]
              [re-frame.api          :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- surface-label
  ; @param (keyword) label-id
  ; @param (map) label-props
  ;  {:disabled? (boolean)(opt)
  ;   :label (metamorphic-content)(opt)
  ;   :placeholder (metamorphic-content)(opt)}
  [label-id {:keys [disabled? label placeholder]}]
  ; Ha nem egy közös elemben (pl. div) volt a sensor és a label, akkor bizonoyos
  ; esetekben (pl. horizontal-polarity elemben) nem megfelelő helyen érzékelt a sensor
  [:div {:data-component :surface-label}
        [surface-a/title-sensor {:title label :offset -12}]
        (let [viewport-large? @(r/subscribe [:x.environment/viewport-large?])]
             [elements/label label-id
                             {:content     label
                              :disabled?   disabled?
                              :font-size   (if viewport-large? :xxl :l)
                              :font-weight :extra-bold
                              :indent      {:left :xs}
                              :line-height :block
                              :placeholder placeholder}])])

(defn component
  ; @param (keyword)(opt) label-id
  ; @param (map) label-props
  ;  {:disabled? (boolean)(opt)
  ;   :label (metamorphic-content)(opt)
  ;   :placeholder (metamorphic-content)(opt)}
  ;
  ; @usage
  ;  [surface-label {...}]
  ;
  ; @usage
  ;  [surface-label :my-surface-label {...}]
  ([label-props]
   [component (random/generate-keyword) label-props])

  ([label-id label-props]
   [surface-label label-id label-props]))
