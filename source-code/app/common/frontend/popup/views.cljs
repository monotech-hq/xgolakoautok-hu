
(ns app.common.frontend.popup.views
    (:require [elements.api     :as elements]
              [mid-fruits.candy :refer [param]]))

;; -- Label-bar components ----------------------------------------------------
;; ----------------------------------------------------------------------------

(defn popup-label-bar-label
  ; @param (keyword) popup-id
  ; @param (map) bar-props
  ;  {:label (metamorphic-content)}
  ;
  ; @usage
  ;  [popup-label-bar-label :my-popup {...}]
  [_ {:keys [label]}]
  [elements/label ::popup-label-bar-label
                  {:content label
                   :indent  {:horizontal :xs}}])

(defn popup-secondary-button
  ; @param (keyword) popup-id
  ; @param (map) bar-props
  ;  {:secondary-button (map)}
  ;
  ; @usage
  ;  [popup-secondary-button :my-popup {...}]
  [_ {:keys [secondary-button]}]
  [elements/button ::popup-secondary-button
                   (merge {:font-size   :xs
                          ;:font-weight :extra-bold
                           :hover-color :highlight
                           :indent      {:horizontal :xxs :vertical :xxs}
                           :keypress    {:key-code 27}}
                          (param secondary-button))])

(defn popup-primary-button
  ; @param (keyword) popup-id
  ; @param (map) bar-props
  ;  {:primary-button (map)}
  ;
  ; @usage
  ;  [popup-primary-button :my-popup {...}]
  [_ {:keys [primary-button]}]
  ; Ha szeretnéd, hogy a popup primary-button gombjának eseménye megtörténjen
  ; az ENTER billentyű lenyomására akkor is, ha egy szövegmező fókuszált
  ; állapotban van, amely állapot letiltja a {:required? false} keypress
  ; eseményeket, akkor az egyes mezők {:on-enter ...} tulajdonságaként
  ; is add meg az eseményt!
  [elements/button ::popup-primary-button
                   (merge {:color       :primary
                           :font-size   :xs
                          ;:font-weight :extra-bold
                           :hover-color :highlight
                           :indent      {:horizontal :xxs :vertical :xxs}
                           :keypress    {:key-code 13}}
                          (param primary-button))])

(defn popup-label-bar
  ; @param (keyword) popup-id
  ; @param (map) bar-props
  ;  {:primary-button (map)
  ;    {:label (metamorphic-content)
  ;     :on-click (metamorphic-even)}
  ;   :secondary-button (map)
  ;    {:label (metamorphic-content)
  ;     :on-click (metamorphic-even)}}
  ;
  ; @usage
  ;  [popup-label-bar :my-popup {...}]
  [popup-id bar-props]
  [elements/horizontal-polarity ::popup-label-bar
                                {:start-content  [popup-secondary-button popup-id bar-props]
                                 :middle-content [popup-label-bar-label  popup-id bar-props]
                                 :end-content    [popup-primary-button   popup-id bar-props]}])

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
                  {:color   (or color :muted)
                   :content label}])

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
