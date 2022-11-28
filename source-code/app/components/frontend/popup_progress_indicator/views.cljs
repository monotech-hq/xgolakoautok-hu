
(ns app.components.frontend.popup-progress-indicator.views
    (:require [elements.api :as elements]
              [random.api   :as random]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- popup-progress-label
  ; @param (keyword) indicator-id
  ; @param (map) indicator-props
  ;  {:color (keyword)(opt)
  ;   :label (metamorphic-content)}
  [_ {:keys [color label]}]
  [elements/label ::popup-progress-label
                  {:color       (or color :muted)
                   :content     label
                   :line-height :block}])

(defn- popup-progress-indicator
  ; @param (keyword) indicator-id
  ; @param (map) indicator-props
  ;  {:color (keyword)(opt)
  ;   :indent (map)
  ;   :label (metamorphic-content)}
  [indicator-id {:keys [indent] :as indicator-props}]
  [elements/column ::popup-progress-indicator
                   {:content             [popup-progress-label indicator-id indicator-props]
                    :horizontal-align    :center
                    :indent              indent
                    :stretch-orientation :vertical
                    :vertical-align      :center}])

(defn component
  ; @param (keyword)(opt) indicator-id
  ; @param (map) indicator-props
  ;  {:background-color (string)(opt)
  ;    Default: "var( --fill-color )"
  ;   :class (keyword or keywords in vector)(opt)
  ;   :content (metamorphic-content)
  ;   :disabled? (boolean)(opt)
  ;    Default: false
  ;   :helper (metamorphic-content)(opt)
  ;   :indent (map)(opt)
  ;   :info-text (metamorphic-content)(opt)
  ;   :label (metamorphic-content)(opt)
  ;   :overflow (keyword)(opt)
  ;    :hidden, :visible
  ;    Default: :visible
  ;   :query (vector)(opt)
  ;   :refresh-interval (ms)(opt)
  ;    W/ {:query ...}
  ;   :style (map)(opt)}
  ;
  ; @usage
  ;  [popup-progress-indicator {...}]
  ;
  ; @usage
  ;  [popup-progress-indicator :my-popup-progress-indicator {...}]
  ([indicator-props]
   [component (random/generate-keyword) indicator-props])

  ([indicator-id indicator-props]
   (let [];indicator-props (popup-progress-indicator.prototypes/indicator-props-prototype indicator-props)
        [popup-progress-indicator indicator-id indicator-props])))
