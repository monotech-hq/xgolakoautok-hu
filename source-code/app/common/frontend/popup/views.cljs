
(ns app.common.frontend.popup.views
    (:require [candy.api    :refer [param]]
              [elements.api :as elements]))

;; -- Label-bar components ----------------------------------------------------
;; ----------------------------------------------------------------------------

;; -- Progress-indicator components ---------------------------------------------
;; ----------------------------------------------------------------------------

(defn popup-progress-label
  ; @param (keyword) popup-id
  ; @param (map) indicator-props
  ;  {:color (keyword)(opt)
  ;   :label (metamorphic-content)}
  ;
  ; @usage
  ;  [popup-progress-label :my-popup {...}]
  [_ {:keys [color label]}]
  [elements/label ::popup-progress-label
                  {:color       (or color :muted)
                   :content     label
                   :line-height :block}])

(defn popup-progress-indicator
  ; @param (keyword) popup-id
  ; @param (map) indicator-props
  ;  {:color (keyword)(opt)
  ;   :indent (map)
  ;   :label (metamorphic-content)}
  ;
  ; @usage
  ;  [popup-progress-indicator :my-popup {...}]
  [popup-id {:keys [indent] :as indicator-props}]
  [elements/column ::popup-progress-indicator
                   {:content             [popup-progress-label popup-id indicator-props]
                    :horizontal-align    :center
                    :indent              indent
                    :stretch-orientation :vertical
                    :vertical-align      :center}])
